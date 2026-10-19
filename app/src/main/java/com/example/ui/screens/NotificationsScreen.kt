package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
  notifications: List<NotificationItem>,
  onBackClick: () -> Unit,
  onNotificationClick: (String) -> Unit,
  onMarkAllRead: () -> Unit,
  onOpenPreferences: () -> Unit,
  onHelpClick: () -> Unit
) {
  val unreadCount = notifications.count { !it.isRead }

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("notifications_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Notifications",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Updates on your reports",
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
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(BackgroundLight),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Unread Header row
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "$unreadCount unread updates",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Text(
            text = "Mark all read",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreen,
            modifier = Modifier
              .clickable { onMarkAllRead() }
              .padding(4.dp)
              .testTag("mark_all_read_button")
          )
        }
      }

      // Notifications list
      items(notifications) { item ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onNotificationClick(item.complaintId) }
            .testTag("notification_card_${item.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (!item.isRead) Color.White else BackgroundLight
          ),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (!item.isRead) ForestGreen.copy(alpha = 0.3f) else OutlineBorder
          )
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = item.timestamp,
                fontSize = 12.sp,
                color = TextMuted
              )

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (!item.isRead) ForestGreenLight else Color.Transparent
              ) {
                Text(
                  text = if (!item.isRead) "Unread" else "Read",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = if (!item.isRead) ForestGreen else TextMuted,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(if (!item.isRead) ForestGreenLight else OutlineBorder.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (item.type) {
                    NotificationType.REFERRAL -> Icons.Default.AltRoute
                    NotificationType.PROGRESS -> Icons.Default.Schedule
                    NotificationType.ASSIGNED -> Icons.Default.AccountBalance
                    NotificationType.RECEIVED -> Icons.Outlined.Inbox
                  },
                  contentDescription = null,
                  tint = if (!item.isRead) ForestGreen else TextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.title,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = item.message,
                  fontSize = 13.sp,
                  color = TextMuted,
                  lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "${item.complaintId} →",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ForestGreen
                )
              }
            }
          }
        }
      }

      // Notification preferences button
      item {
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = onOpenPreferences,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("notification_preferences_button"),
          shape = RoundedCornerShape(24.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
          colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Notification preferences",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreen
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
