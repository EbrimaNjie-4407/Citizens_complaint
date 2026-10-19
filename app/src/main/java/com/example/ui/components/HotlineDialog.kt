package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

data class HotlineContact(
  val institution: String,
  val service: String,
  val number: String,
  val isEmergency: Boolean = false
)

@Composable
fun HotlineDialog(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val hotlines = listOf(
    HotlineContact("NAWEC Public Emergency", "Water bursts & electrical hazards", "114", true),
    HotlineContact("The Gambia Police Force", "Security & traffic emergencies", "117", true),
    HotlineContact("Gambia Fire & Rescue", "Fire emergencies & rescue", "118", true),
    HotlineContact("KMC Cleansing Services", "Municipal solid waste & drainage", "112"),
    HotlineContact("National Roads Authority", "Road cave-ins & bridge obstruction", "123")
  )

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("hotline_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ForestGreenLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "Direct Hotline",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
              Text(
                text = "Official emergency services",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "For active hazards or life-threatening public incidents, call the direct emergency service immediately:",
          fontSize = 13.sp,
          color = TextMuted,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        hotlines.forEach { item ->
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                  data = Uri.parse("tel:${item.number}")
                }
                context.startActivity(intent)
              },
            shape = RoundedCornerShape(12.dp),
            color = if (item.isEmergency) UrgencyCriticalBg.copy(alpha = 0.4f) else BackgroundLight
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.institution,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = TextDark
                )
                Text(
                  text = item.service,
                  fontSize = 12.sp,
                  color = TextMuted
                )
              }

              Button(
                onClick = {
                  val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${item.number}")
                  }
                  context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (item.isEmergency) ForestGreen else ForestGreenDark
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp)
              ) {
                Text(
                  text = "Call ${item.number}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = BackgroundLight),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "Back to Report",
            color = TextDark,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}
