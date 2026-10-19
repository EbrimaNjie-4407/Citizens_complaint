package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
  userProfile: UserProfile,
  onBackClick: () -> Unit,
  onUpdatePreferences: (
    reportUpdates: Boolean,
    smsNotifications: Boolean,
    communityAlerts: Boolean
  ) -> Unit,
  onEditProfile: () -> Unit,
  onLanguageClick: () -> Unit,
  onPrivacyClick: () -> Unit,
  onHelpClick: () -> Unit,
  onSignOut: () -> Unit
) {
  var reportUpdates by remember(userProfile) { mutableStateOf(userProfile.reportUpdates) }
  var smsNotifications by remember(userProfile) { mutableStateOf(userProfile.smsNotifications) }
  var communityAlerts by remember(userProfile) { mutableStateOf(userProfile.communityAlerts) }

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(ForestGreen)
          .padding(horizontal = 12.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("profile_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Profile & Preferences",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Your account, your choices",
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.85f)
            )
          }

          IconButton(onClick = onHelpClick) {
            Icon(Icons.Default.HelpOutline, contentDescription = "Help", tint = Color.White)
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(BackgroundLight)
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // User Profile Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(ForestGreenLight),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = userProfile.fullName.take(1).uppercase(),
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = ForestGreen
            )
          }

          Spacer(modifier = Modifier.width(16.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = userProfile.fullName,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = userProfile.phoneNumber,
              fontSize = 13.sp,
              color = TextMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Edit profile",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = ForestGreen,
              modifier = Modifier
                .clickable { onEditProfile() }
                .testTag("edit_profile_link")
            )
          }
        }
      }

      // NOTIFICATIONS Section
      Text(
        text = "NOTIFICATIONS",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 1.sp
      )

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Report updates
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Report updates",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
              )
              Text(
                text = "Acknowledgements, assignments and referrals",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
            Switch(
              checked = reportUpdates,
              onCheckedChange = {
                reportUpdates = it
                onUpdatePreferences(reportUpdates, smsNotifications, communityAlerts)
              },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ForestGreen
              ),
              modifier = Modifier.testTag("switch_report_updates")
            )
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = DividerColor)
          Spacer(modifier = Modifier.height(14.dp))

          // SMS notifications
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "SMS notifications",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
              )
              Text(
                text = "Send updates to your phone",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
            Switch(
              checked = smsNotifications,
              onCheckedChange = {
                smsNotifications = it
                onUpdatePreferences(reportUpdates, smsNotifications, communityAlerts)
              },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ForestGreen
              ),
              modifier = Modifier.testTag("switch_sms_notifications")
            )
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = DividerColor)
          Spacer(modifier = Modifier.height(14.dp))

          // Community alerts
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Community alerts",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
              )
              Text(
                text = "General service announcements",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
            Switch(
              checked = communityAlerts,
              onCheckedChange = {
                communityAlerts = it
                onUpdatePreferences(reportUpdates, smsNotifications, communityAlerts)
              },
              colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ForestGreen
              ),
              modifier = Modifier.testTag("switch_community_alerts")
            )
          }
        }
      }

      // PREFERENCES & SUPPORT Section
      Text(
        text = "PREFERENCES & SUPPORT",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 1.sp
      )

      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
      ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
          // Language
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onLanguageClick() }
              .padding(vertical = 12.dp)
              .testTag("preference_language_row"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Translate, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Language", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextDark, modifier = Modifier.weight(1f))
            Text(userProfile.language, fontSize = 14.sp, color = TextMuted)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
          }

          HorizontalDivider(color = DividerColor)

          // Privacy & permissions
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onPrivacyClick() }
              .padding(vertical = 12.dp)
              .testTag("preference_privacy_row"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Privacy & permissions", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextDark, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
          }

          HorizontalDivider(color = DividerColor)

          // Help & support
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onHelpClick() }
              .padding(vertical = 12.dp)
              .testTag("preference_help_row"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Help & support", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextDark, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Sign out / Switch Account Button
      OutlinedButton(
        onClick = onSignOut,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("sign_out_button"),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ExitToApp,
          contentDescription = null,
          tint = ForestGreen,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (userProfile.isGuest) "Switch to Citizen Sign-In" else "Sign out",
          color = ForestGreen,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
