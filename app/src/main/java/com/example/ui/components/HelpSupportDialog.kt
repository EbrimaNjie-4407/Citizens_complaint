package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun HelpSupportDialog(
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
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
            Icon(
              imageVector = Icons.Default.HelpOutline,
              contentDescription = null,
              tint = ForestGreen,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "Citizen Help & FAQs",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
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

        FaqItem(
          question = "Do I need an account to report?",
          answer = "No. Citizen Gambia supports Guest reporting without registration. When you submit, you receive a Reference ID (like CG-2026-01482) which allows you to track progress at any time."
        )

        FaqItem(
          question = "How are complaints routed?",
          answer = "Complaints are assigned directly to responsible Gambian agencies including NAWEC (water/electricity), National Roads Authority (potholes/highways), Kanifing Municipal Council (waste/sanitation), Banjul City Council, or Brikama Area Council."
        )

        FaqItem(
          question = "Is GPS required?",
          answer = "No. GPS location is completely optional. You can enter an address or nearby landmark manually (e.g. 'Main entrance, beside taxi stand') if you prefer."
        )

        FaqItem(
          question = "What does 'Urgency' mean?",
          answer = "Urgency informs crews whether there is an active public hazard (e.g. exposed live cables, deep road collapse, or burst pipes). It does not alter complaint status."
        )

        FaqItem(
          question = "How can I follow up?",
          answer = "Use the 'Track Report' feature from the home screen or navigation bar, enter your Reference ID, or review alerts sent to your device."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Got it", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun FaqItem(question: String, answer: String) {
  Column(modifier = Modifier.padding(bottom = 12.dp)) {
    Text(
      text = question,
      fontSize = 14.sp,
      fontWeight = FontWeight.SemiBold,
      color = TextDark
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = answer,
      fontSize = 12.sp,
      color = TextMuted,
      lineHeight = 17.sp
    )
  }
}
