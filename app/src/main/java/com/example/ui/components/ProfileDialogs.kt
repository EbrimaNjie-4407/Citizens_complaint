package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun EditProfileDialog(
  currentName: String,
  currentPhone: String,
  onDismiss: () -> Unit,
  onSave: (String, String) -> Unit
) {
  var name by remember { mutableStateOf(currentName) }
  var phone by remember { mutableStateOf(currentPhone) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("edit_profile_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Edit Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Full Name") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Phone Number") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = { onSave(name, phone) },
          modifier = Modifier.fillMaxWidth().height(48.dp),
          shape = RoundedCornerShape(24.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White)
        }
      }
    }
  }
}

@Composable
fun PrivacyDialog(
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Security, contentDescription = null, tint = ForestGreen)
            Text("Privacy & Permissions", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextDark)
          }
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Citizen Gambia is built with citizen safety and public accountability as primary values:",
          fontSize = 13.sp,
          color = TextDark,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "• Location (GPS): Only accessed when setting or confirming an issue pin. Never tracked in background.\n\n" +
            "• Camera & Photos: Used exclusively for capturing and uploading evidence photos of community issues.\n\n" +
            "• Microphone: Used only while pressing the record button to attach a spoken explanation to your complaint.\n\n" +
            "• Public Tracking: Guest tracking hides personal telephone numbers and private identity data.",
          fontSize = 12.sp,
          color = TextMuted,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Close", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun LanguageDialog(
  currentLanguage: String,
  onDismiss: () -> Unit,
  onSelectLanguage: (String) -> Unit
) {
  val languages = listOf("English", "Wolof", "Mandinka", "Fula")

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Select Language", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
          IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        languages.forEach { lang ->
          val isSelected = currentLanguage.equals(lang, ignoreCase = true)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelectLanguage(lang) }
              .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = lang,
              fontSize = 15.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) ForestGreen else TextDark
            )
            if (isSelected) {
              Icon(Icons.Default.Check, contentDescription = "Selected", tint = ForestGreen)
            }
          }
          HorizontalDivider(color = DividerColor)
        }
      }
    }
  }
}
