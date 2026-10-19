package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.IssueCategory
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CitizenGambiaApp()
      }
    }
  }
}

@Composable
fun CitizenGambiaApp(viewModel: MainViewModel = viewModel()) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val navDestination by viewModel.currentNavDestination.collectAsStateWithLifecycle()
  val complaints by viewModel.repository.complaints.collectAsStateWithLifecycle()
  val notifications by viewModel.repository.notifications.collectAsStateWithLifecycle()
  val userProfile by viewModel.repository.userProfile.collectAsStateWithLifecycle()
  val selectedComplaintId by viewModel.selectedComplaintId.collectAsStateWithLifecycle()
  val lastSubmitted by viewModel.lastSubmittedComplaint.collectAsStateWithLifecycle()

  val showHotline by viewModel.showHotlineDialog.collectAsStateWithLifecycle()
  val showHelp by viewModel.showHelpDialog.collectAsStateWithLifecycle()
  val showEditProfile by viewModel.showEditProfileDialog.collectAsStateWithLifecycle()
  val showPrivacy by viewModel.showPrivacyDialog.collectAsStateWithLifecycle()
  val showLanguage by viewModel.showLanguageDialog.collectAsStateWithLifecycle()

  val draftCategory by viewModel.draftCategory.collectAsStateWithLifecycle()
  val draftLocationName by viewModel.draftLocationName.collectAsStateWithLifecycle()
  val draftLocationDetails by viewModel.draftLocationDetails.collectAsStateWithLifecycle()
  val draftLandmark by viewModel.draftLandmark.collectAsStateWithLifecycle()
  val infraFilter by viewModel.infraFilter.collectAsStateWithLifecycle()

  val unreadNotificationsCount = notifications.count { !it.isRead }

  // Global BackHandler
  BackHandler(enabled = currentScreen != AppScreen.HOME && currentScreen != AppScreen.WELCOME) {
    viewModel.navigateBack()
  }

  // Dialogs
  if (showHotline) {
    HotlineDialog(onDismiss = { viewModel.showHotlineDialog.value = false })
  }

  if (showHelp) {
    HelpSupportDialog(onDismiss = { viewModel.showHelpDialog.value = false })
  }

  if (showEditProfile) {
    EditProfileDialog(
      currentName = userProfile.fullName,
      currentPhone = userProfile.phoneNumber,
      onDismiss = { viewModel.showEditProfileDialog.value = false },
      onSave = { name, phone ->
        viewModel.repository.updateUserProfile(
          fullName = name,
          phoneNumber = phone,
          isGuest = userProfile.isGuest,
          reportUpdates = userProfile.reportUpdates,
          smsNotifications = userProfile.smsNotifications,
          communityAlerts = userProfile.communityAlerts,
          language = userProfile.language
        )
        viewModel.showEditProfileDialog.value = false
      }
    )
  }

  if (showPrivacy) {
    PrivacyDialog(onDismiss = { viewModel.showPrivacyDialog.value = false })
  }

  if (showLanguage) {
    LanguageDialog(
      currentLanguage = userProfile.language,
      onDismiss = { viewModel.showLanguageDialog.value = false },
      onSelectLanguage = { lang ->
        viewModel.repository.updateUserProfile(
          fullName = userProfile.fullName,
          phoneNumber = userProfile.phoneNumber,
          isGuest = userProfile.isGuest,
          reportUpdates = userProfile.reportUpdates,
          smsNotifications = userProfile.smsNotifications,
          communityAlerts = userProfile.communityAlerts,
          language = lang
        )
        viewModel.showLanguageDialog.value = false
      }
    )
  }

  // Show Bottom Nav on screens where bottom nav is present in PDF
  val showBottomNav = currentScreen in listOf(
    AppScreen.HOME,
    AppScreen.MY_REPORTS,
    AppScreen.NOTIFICATIONS,
    AppScreen.PROFILE,
    AppScreen.CHOOSE_INFRASTRUCTURE,
    AppScreen.INFRASTRUCTURE_STATUS,
    AppScreen.QUICK_REPORT,
    AppScreen.REPORT_DETAILS
  )

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(BackgroundLight),
    bottomBar = {
      if (showBottomNav) {
        CitizenBottomNavBar(
          currentDestination = navDestination,
          onNavigate = { viewModel.onNavSelected(it) },
          unreadAlertsCount = unreadNotificationsCount
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        AppScreen.WELCOME -> {
          WelcomeScreen(
            onSignIn = { name, phone -> viewModel.signIn(name, phone) },
            onContinueAsGuest = { viewModel.continueAsGuest() },
            onQuickReport = {
              viewModel.draftCategory.value = IssueCategory.WASTE
              viewModel.navigateTo(AppScreen.QUICK_REPORT)
            }
          )
        }

        AppScreen.HOME -> {
          HomeScreen(
            userProfile = userProfile,
            recentComplaints = complaints,
            onReportIssue = {
              viewModel.draftCategory.value = IssueCategory.WASTE
              viewModel.navigateTo(AppScreen.QUICK_REPORT)
            },
            onTrackReportShortcut = { viewModel.navigateTo(AppScreen.TRACK_REPORT) },
            onCategorySelected = { cat ->
              viewModel.draftCategory.value = cat
              viewModel.navigateTo(AppScreen.QUICK_REPORT)
            },
            onMoreCategories = { viewModel.navigateTo(AppScreen.CHOOSE_INFRASTRUCTURE) },
            onComplaintClick = { id -> viewModel.openReportDetails(id) },
            onSeeAllComplaints = { viewModel.navigateTo(AppScreen.MY_REPORTS) },
            onAlertBannerClick = { id -> viewModel.openReportDetails(id) },
            onOpenInfrastructureStatus = { viewModel.navigateTo(AppScreen.INFRASTRUCTURE_STATUS) },
            onOpenEmergencyContacts = { viewModel.showHotlineDialog.value = true }
          )
        }

        AppScreen.QUICK_REPORT -> {
          QuickReportScreen(
            initialCategory = draftCategory,
            currentLocationName = draftLocationName,
            currentLocationDetails = draftLocationDetails,
            onBackClick = { viewModel.navigateBack() },
            onOpenHotline = { viewModel.showHotlineDialog.value = true },
            onChangeLocation = { viewModel.navigateTo(AppScreen.SET_LOCATION) },
            onReviewAndSubmit = { title, category, description, locName, locDetails, urgency, hasPhoto, photoUri, hasVoiceNote, voiceDuration ->
              viewModel.submitReport(
                title = title,
                category = category,
                description = description,
                locationName = locName,
                locationDetails = locDetails,
                urgency = urgency,
                hasPhoto = hasPhoto,
                photoUri = photoUri,
                hasVoiceNote = hasVoiceNote,
                voiceDuration = voiceDuration
              )
            }
          )
        }

        AppScreen.SET_LOCATION -> {
          SetLocationScreen(
            initialLocationName = draftLocationName,
            initialLandmark = draftLandmark,
            onBackClick = { viewModel.navigateBack() },
            onHelpClick = { viewModel.showHelpDialog.value = true },
            onConfirmLocation = { locName, landmark ->
              viewModel.draftLocationName.value = locName
              viewModel.draftLandmark.value = landmark
              viewModel.draftLocationDetails.value = "$locName, The Gambia · $landmark"
              viewModel.navigateBack()
            }
          )
        }

        AppScreen.REPORT_SUBMITTED -> {
          val active = lastSubmitted ?: complaints.firstOrNull()
          ReportSubmittedScreen(
            complaintId = active?.id ?: "CG-2026-01482",
            submissionDate = active?.createdAt ?: "12 Jul 2026 · 10:24",
            onTrackReport = { id -> viewModel.openReportDetails(id) },
            onReturnHome = { viewModel.navigateTo(AppScreen.HOME) },
            onHelpClick = { viewModel.showHelpDialog.value = true }
          )
        }

        AppScreen.REPORT_DETAILS -> {
          val targetId = selectedComplaintId ?: "CG-2026-01482"
          val complaint = viewModel.repository.getComplaintById(targetId)
            ?: complaints.firstOrNull()
            ?: return@Box

          ReportDetailsScreen(
            complaint = complaint,
            onBackClick = { viewModel.navigateBack() },
            onGiveFeedback = { id -> viewModel.openGiveFeedback(id) },
            onRequestReview = { id -> viewModel.openRequestReview(id) },
            onHelpClick = { viewModel.showHelpDialog.value = true },
            onAdvanceFieldStep = { id -> viewModel.advanceFieldStep(id) }
          )
        }

        AppScreen.MY_REPORTS -> {
          MyReportsScreen(
            complaints = complaints,
            onBackClick = { viewModel.navigateBack() },
            onComplaintClick = { id -> viewModel.openReportDetails(id) },
            onTrackByIdClick = { viewModel.navigateTo(AppScreen.TRACK_REPORT) },
            onHelpClick = { viewModel.showHelpDialog.value = true }
          )
        }

        AppScreen.TRACK_REPORT -> {
          TrackReportScreen(
            initialId = selectedComplaintId ?: "CG-2026-01482",
            complaints = complaints,
            onBackClick = { viewModel.navigateBack() },
            onHelpClick = { viewModel.showHelpDialog.value = true },
            onTrackSubmitted = { id ->
              val found = viewModel.trackReportById(id)
              if (!found) {
                Toast.makeText(
                  viewModel.getApplication(),
                  "Complaint reference '$id' not found in database. Try CG-2026-01482",
                  Toast.LENGTH_LONG
                ).show()
              }
            },
            onOpenDetails = { id -> viewModel.openReportDetails(id) }
          )
        }

        AppScreen.NOTIFICATIONS -> {
          NotificationsScreen(
            notifications = notifications,
            onBackClick = { viewModel.navigateBack() },
            onNotificationClick = { complaintId -> viewModel.openReportDetails(complaintId) },
            onMarkAllRead = { viewModel.repository.markAllNotificationsRead() },
            onOpenPreferences = { viewModel.navigateTo(AppScreen.PROFILE) },
            onHelpClick = { viewModel.showHelpDialog.value = true }
          )
        }

        AppScreen.PROFILE -> {
          ProfileScreen(
            userProfile = userProfile,
            onBackClick = { viewModel.navigateBack() },
            onUpdatePreferences = { reportUpdates, smsNotifs, communityAlerts ->
              viewModel.repository.updateUserProfile(
                fullName = userProfile.fullName,
                phoneNumber = userProfile.phoneNumber,
                isGuest = userProfile.isGuest,
                reportUpdates = reportUpdates,
                smsNotifications = smsNotifs,
                communityAlerts = communityAlerts,
                language = userProfile.language
              )
            },
            onEditProfile = { viewModel.showEditProfileDialog.value = true },
            onLanguageClick = { viewModel.showLanguageDialog.value = true },
            onPrivacyClick = { viewModel.showPrivacyDialog.value = true },
            onHelpClick = { viewModel.showHelpDialog.value = true },
            onSignOut = { viewModel.signOut() }
          )
        }

        AppScreen.CHOOSE_INFRASTRUCTURE -> {
          ChooseInfrastructureScreen(
            infrastructures = viewModel.repository.infrastructures,
            initialFilter = infraFilter,
            onBackClick = { viewModel.navigateBack() },
            onSelectInfrastructure = { infra ->
              val cat = when (infra.categoryTag.lowercase()) {
                "water" -> IssueCategory.WATER
                "road" -> IssueCategory.ROADS
                "gsm" -> IssueCategory.GSM
                "electricity" -> IssueCategory.ELECTRICITY
                else -> IssueCategory.WASTE
              }
              viewModel.draftCategory.value = cat
              viewModel.navigateTo(AppScreen.QUICK_REPORT)
            }
          )
        }

        AppScreen.INFRASTRUCTURE_STATUS -> {
          InfrastructureStatusScreen(
            sectors = viewModel.repository.sectorConditions,
            onBackClick = { viewModel.navigateBack() },
            onOpenReportForSector = { sectorId ->
              val cat = when (sectorId.lowercase()) {
                "electricity" -> IssueCategory.ELECTRICITY
                "water" -> IssueCategory.WATER
                "roads" -> IssueCategory.ROADS
                "waste" -> IssueCategory.WASTE
                "gsm" -> IssueCategory.GSM
                else -> IssueCategory.OTHER
              }
              viewModel.draftCategory.value = cat
              viewModel.navigateTo(AppScreen.QUICK_REPORT)
            }
          )
        }

        AppScreen.GIVE_FEEDBACK -> {
          val targetId = selectedComplaintId ?: "CG-2026-01482"
          val complaint = viewModel.repository.getComplaintById(targetId)
            ?: complaints.firstOrNull()
            ?: return@Box

          GiveFeedbackScreen(
            complaint = complaint,
            onBackClick = { viewModel.navigateBack() },
            onHelpClick = { viewModel.showHelpDialog.value = true },
            onSubmitFeedback = { rating, comments ->
              viewModel.submitFeedback(complaint.id, rating, comments)
              viewModel.navigateBack()
            }
          )
        }

        AppScreen.REQUEST_REVIEW -> {
          val targetId = selectedComplaintId ?: "CG-2026-01482"
          val complaint = viewModel.repository.getComplaintById(targetId)
            ?: complaints.firstOrNull()
            ?: return@Box

          RequestReviewScreen(
            complaint = complaint,
            onBackClick = { viewModel.navigateBack() },
            onHelpClick = { viewModel.showHelpDialog.value = true },
            onSubmitReview = { reason, explanation, extraEvidence ->
              viewModel.submitReview(complaint.id, reason, explanation, extraEvidence)
              viewModel.navigateBack()
            }
          )
        }
      }
    }
  }
}
