package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.ComplaintRepository
import com.example.ui.components.NavDestination
import com.example.util.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
  WELCOME,
  HOME,
  QUICK_REPORT,
  SET_LOCATION,
  REPORT_SUBMITTED,
  REPORT_DETAILS,
  MY_REPORTS,
  TRACK_REPORT,
  NOTIFICATIONS,
  PROFILE,
  CHOOSE_INFRASTRUCTURE,
  INFRASTRUCTURE_STATUS,
  GIVE_FEEDBACK,
  REQUEST_REVIEW,
  ADMIN_DASHBOARD,
  ADMIN_COMPLAINT_DETAIL,
  INSTITUTIONAL_ROUTING
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

  val repository = ComplaintRepository(application)

  private val _currentScreen = MutableStateFlow(AppScreen.HOME)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val screenBackStack = mutableListOf<AppScreen>()

  private val _currentNavDestination = MutableStateFlow(NavDestination.HOME)
  val currentNavDestination: StateFlow<NavDestination> = _currentNavDestination.asStateFlow()

  // Selected complaint for details / feedback / review
  private val _selectedComplaintId = MutableStateFlow<String?>("CG-2026-01482")
  val selectedComplaintId: StateFlow<String?> = _selectedComplaintId.asStateFlow()

  // Newly submitted complaint ID
  private val _lastSubmittedComplaint = MutableStateFlow<Complaint?>(null)
  val lastSubmittedComplaint: StateFlow<Complaint?> = _lastSubmittedComplaint.asStateFlow()

  // Draft state for Quick Report
  val draftCategory = MutableStateFlow(IssueCategory.WASTE)
  val draftLocationName = MutableStateFlow("Serrekunda Market")
  val draftLocationDetails = MutableStateFlow("Kanifing, The Gambia · GPS Auto-detected (±3m)")
  val draftLandmark = MutableStateFlow("Main entrance, beside the taxi stand")
  val isDetectingLocation = MutableStateFlow(false)

  // Infrastructure filter selection
  val infraFilter = MutableStateFlow("All")

  // Dialog States
  val showHotlineDialog = MutableStateFlow(false)
  val showHelpDialog = MutableStateFlow(false)
  val showEditProfileDialog = MutableStateFlow(false)
  val showPrivacyDialog = MutableStateFlow(false)
  val showLanguageDialog = MutableStateFlow(false)

  init {
    // Automatically trigger GPS detection on startup
    performAutoDetectLocation(application)
  }

  fun performAutoDetectLocation(context: Context) {
    viewModelScope.launch {
      isDetectingLocation.value = true
      val loc = LocationHelper.autoDetectCurrentLocation(context)
      draftLocationName.value = loc.locationName
      draftLocationDetails.value = "${loc.localityDetails} · ${loc.source}"
      draftLandmark.value = loc.landmark
      isDetectingLocation.value = false
    }
  }

  fun navigateTo(screen: AppScreen) {
    if (_currentScreen.value != screen) {
      screenBackStack.add(_currentScreen.value)
      _currentScreen.value = screen
    }

    when (screen) {
      AppScreen.HOME -> _currentNavDestination.value = NavDestination.HOME
      AppScreen.MY_REPORTS -> _currentNavDestination.value = NavDestination.MY_REPORTS
      AppScreen.NOTIFICATIONS -> _currentNavDestination.value = NavDestination.ALERTS
      AppScreen.PROFILE -> _currentNavDestination.value = NavDestination.PROFILE
      else -> Unit
    }
  }

  fun navigateBack(): Boolean {
    if (screenBackStack.isNotEmpty()) {
      val prev = screenBackStack.removeAt(screenBackStack.size - 1)
      _currentScreen.value = prev
      when (prev) {
        AppScreen.HOME -> _currentNavDestination.value = NavDestination.HOME
        AppScreen.MY_REPORTS -> _currentNavDestination.value = NavDestination.MY_REPORTS
        AppScreen.NOTIFICATIONS -> _currentNavDestination.value = NavDestination.ALERTS
        AppScreen.PROFILE -> _currentNavDestination.value = NavDestination.PROFILE
        else -> Unit
      }
      return true
    } else if (_currentScreen.value != AppScreen.HOME && _currentScreen.value != AppScreen.WELCOME) {
      _currentScreen.value = AppScreen.HOME
      _currentNavDestination.value = NavDestination.HOME
      return true
    }
    return false
  }

  fun onNavSelected(destination: NavDestination) {
    _currentNavDestination.value = destination
    when (destination) {
      NavDestination.HOME -> navigateTo(AppScreen.HOME)
      NavDestination.MY_REPORTS -> navigateTo(AppScreen.MY_REPORTS)
      NavDestination.ALERTS -> navigateTo(AppScreen.NOTIFICATIONS)
      NavDestination.PROFILE -> navigateTo(AppScreen.PROFILE)
    }
  }

  fun openReportDetails(complaintId: String) {
    _selectedComplaintId.value = complaintId
    navigateTo(AppScreen.REPORT_DETAILS)
  }

  fun advanceFieldStep(complaintId: String) {
    viewModelScope.launch {
      repository.advancePipelineStage(complaintId)
    }
  }

  fun openGiveFeedback(complaintId: String) {
    _selectedComplaintId.value = complaintId
    navigateTo(AppScreen.GIVE_FEEDBACK)
  }

  fun openRequestReview(complaintId: String) {
    _selectedComplaintId.value = complaintId
    navigateTo(AppScreen.REQUEST_REVIEW)
  }

  // Saved Draft flow
  val savedDraft = repository.savedDraft

  fun saveDraft(draft: ComplaintDraft) {
    repository.saveDraft(draft)
  }

  fun clearDraft() {
    repository.clearDraft()
  }

  fun submitReport(
    title: String,
    category: IssueCategory,
    description: String,
    locationName: String,
    locationDetails: String,
    urgency: UrgencyLevel,
    hasPhoto: Boolean,
    photoUri: String?,
    hasVoiceNote: Boolean,
    voiceDuration: String?,
    categoryId: String = "waste_management",
    subcategory: String? = null,
    region: String = "Greater Banjul Area (GBA)",
    district: String = "Kanifing Municipality",
    community: String = "Serrekunda Central"
  ) {
    viewModelScope.launch {
      val created = repository.createComplaint(
        title = title,
        category = category,
        description = description,
        locationName = locationName,
        locationDetails = locationDetails,
        urgency = urgency,
        photoUri = photoUri,
        hasPhoto = hasPhoto,
        hasVoiceNote = hasVoiceNote,
        voiceDuration = voiceDuration,
        categoryId = categoryId,
        subcategory = subcategory,
        region = region,
        district = district,
        community = community
      )
      _lastSubmittedComplaint.value = created
      _selectedComplaintId.value = created.id
      navigateTo(AppScreen.REPORT_SUBMITTED)
    }
  }

  // --- Administrative and Institutional Actions ---

  fun openAdminComplaintDetail(complaintId: String) {
    _selectedComplaintId.value = complaintId
    navigateTo(AppScreen.ADMIN_COMPLAINT_DETAIL)
  }

  fun switchUserRole(role: UserRole, instId: String? = null, instName: String? = null) {
    repository.updateUserRole(role, instId, instName)
  }

  fun assignInstitution(complaintId: String, institutionId: String, institutionName: String, officerName: String?) {
    viewModelScope.launch {
      repository.assignInstitution(complaintId, institutionId, institutionName, officerName)
    }
  }

  fun escalateSeverity(complaintId: String, urgency: UrgencyLevel, reason: String) {
    viewModelScope.launch {
      repository.escalateSeverity(complaintId, urgency, reason)
    }
  }

  fun flagDuplicate(complaintId: String, duplicateOfId: String) {
    viewModelScope.launch {
      repository.flagDuplicate(complaintId, duplicateOfId)
    }
  }

  fun postInstitutionalUpdate(complaintId: String, quote: String, author: String) {
    viewModelScope.launch {
      repository.postInstitutionalUpdate(complaintId, quote, author)
    }
  }

  fun resolveWithEvidence(complaintId: String, notes: String, actions: List<String>, equipment: List<String>) {
    viewModelScope.launch {
      repository.resolveWithEvidence(complaintId, notes, actions, equipment)
    }
  }

  fun updateRoutingRule(rule: InstitutionalRoutingRule) {
    repository.updateRoutingRule(rule)
  }

  fun resetRoutingRulesToDefault() {
    repository.resetRoutingRulesToDefault()
  }

  fun submitFeedback(complaintId: String, rating: SatisfactionLevel, comments: String) {
    viewModelScope.launch {
      repository.addFeedback(complaintId, rating, comments)
    }
  }

  fun submitReview(complaintId: String, reason: String, explanation: String, extraEvidence: String?) {
    viewModelScope.launch {
      repository.addReviewRequest(complaintId, reason, explanation, extraEvidence)
    }
  }

  fun trackReportById(complaintId: String): Boolean {
    val found = repository.getComplaintById(complaintId)
    if (found != null) {
      _selectedComplaintId.value = found.id
      navigateTo(AppScreen.REPORT_DETAILS)
      return true
    }
    return false
  }

  fun signIn(name: String, phone: String) {
    repository.updateUserProfile(
      fullName = name.ifBlank { "Citizen" },
      phoneNumber = phone.ifBlank { "+220 000 0000" },
      isGuest = false,
      reportUpdates = true,
      smsNotifications = true,
      communityAlerts = false,
      language = "English"
    )
    navigateTo(AppScreen.HOME)
  }

  fun continueAsGuest() {
    repository.updateUserProfile(
      fullName = "Citizen",
      phoneNumber = "Guest User",
      isGuest = true,
      reportUpdates = true,
      smsNotifications = false,
      communityAlerts = false,
      language = "English"
    )
    navigateTo(AppScreen.HOME)
  }

  fun signOut() {
    repository.updateUserProfile(
      fullName = "Citizen",
      phoneNumber = "",
      isGuest = true,
      reportUpdates = true,
      smsNotifications = false,
      communityAlerts = false,
      language = "English"
    )
    navigateTo(AppScreen.WELCOME)
  }
}
