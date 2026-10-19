package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun RequestReviewScreen(
  complaint: Complaint,
  onBackClick: () -> Unit,
  onHelpClick: () -> Unit,
  onSubmitReview: (reason: String, explanation: String, extraEvidence: String?) -> Unit
) {
  val context = LocalContext.current
  val reasons = listOf(
    "I need a clearer progress update",
    "The issue or location is incorrect",
    "I disagree with an institutional update",
    "Other reason"
  )

  var selectedReason by remember { mutableStateOf(reasons[0]) }
  var explanation by remember {
    mutableStateOf("Please clarify whether a team has visited the market and what happens next.")
  }
  var extraEvidenceAttached by remember { mutableStateOf(false) }

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("request_review_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Request Review",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Ask for another look · No login needed",
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
      // Complaint summary card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = complaint.id,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = ForestGreen
            )
            StatusBadge(status = complaint.status)
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = complaint.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "${complaint.locationName} · ${complaint.assignedInstitution ?: "KMC"}",
            fontSize = 12.sp,
            color = TextMuted
          )
        }
      }

      // Headline
      Column {
        Text(
          text = "What needs another look?",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Choose the reason that best describes your concern. A review request does not mean the issue is immediately resolved.",
          fontSize = 13.sp,
          color = TextMuted,
          lineHeight = 18.sp
        )
      }

      // Reason Radios
      reasons.forEach { reason ->
        val isSelected = selectedReason == reason

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedReason = reason }
            .testTag("review_reason_${reason.take(8).lowercase()}"),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) ForestGreen else OutlineBorder
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = isSelected,
              onClick = { selectedReason = reason },
              colors = RadioButtonDefaults.colors(
                selectedColor = ForestGreen,
                unselectedColor = TextMuted
              )
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
              text = reason,
              fontSize = 14.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
              color = TextDark
            )
          }
        }
      }

      // Tell us more · Optional
      Column {
        Text(
          text = "Tell us more · Optional",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = explanation,
          onValueChange = { explanation = it },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("review_explanation_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = OutlineBorder,
            focusedBorderColor = ForestGreen
          )
        )
      }

      // Extra evidence · Optional
      Column {
        Text(
          text = "Extra evidence · Optional",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
          onClick = {
            extraEvidenceAttached = !extraEvidenceAttached
            if (extraEvidenceAttached) {
              Toast.makeText(context, "Photo/voice note attachment simulated", Toast.LENGTH_SHORT).show()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("add_extra_evidence_button"),
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (extraEvidenceAttached) ForestGreenLight else Color.White
          )
        ) {
          Icon(
            imageVector = Icons.Outlined.AttachFile,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (extraEvidenceAttached) "Photo/Audio evidence attached" else "Add photo or voice note",
            color = ForestGreen,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          )
        }
      }

      // What happens next? Callout card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenLight.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier.size(20.dp)
          )
          Column {
            Text(
              text = "What happens next?",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Your request will be sent for review with this complaint. It does not change the current status. No registration is required.",
              fontSize = 12.sp,
              color = TextMuted,
              lineHeight = 17.sp
            )
          }
        }
      }

      // Send Request Button
      Button(
        onClick = {
          onSubmitReview(
            selectedReason,
            explanation,
            if (extraEvidenceAttached) "Attached supplementary evidence" else null
          )
          Toast.makeText(context, "Review request sent!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("send_review_request_button"),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
      ) {
        Icon(
          imageVector = Icons.Default.NearMe,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Send Request",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
