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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentNeutral
import androidx.compose.material.icons.outlined.SentimentSatisfied
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
import com.example.data.model.SatisfactionLevel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun GiveFeedbackScreen(
  complaint: Complaint,
  onBackClick: () -> Unit,
  onHelpClick: () -> Unit,
  onSubmitFeedback: (SatisfactionLevel, String) -> Unit
) {
  val context = LocalContext.current
  var selectedRating by remember { mutableStateOf(SatisfactionLevel.SOMEWHAT_SATISFIED) }
  var comments by remember {
    mutableStateOf("The acknowledgement was helpful. I’d appreciate a clearer update on collection.")
  }
  var isSubmitted by remember { mutableStateOf(false) }

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("give_feedback_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Give Feedback",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Tell us about your experience",
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
      // Complaint Card
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

      // Question Headline
      Column {
        Text(
          text = "How satisfied are you with the handling so far?",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Your report is still in progress. Rate the service and updates, not a completed resolution.",
          fontSize = 13.sp,
          color = TextMuted,
          lineHeight = 18.sp
        )
      }

      // 3 Satisfaction choices
      val choices = listOf(
        Triple(SatisfactionLevel.NOT_SATISFIED, "Not satisfied", Icons.Outlined.SentimentDissatisfied),
        Triple(SatisfactionLevel.SOMEWHAT_SATISFIED, "Somewhat satisfied", Icons.Outlined.SentimentNeutral),
        Triple(SatisfactionLevel.SATISFIED, "Satisfied", Icons.Outlined.SentimentSatisfied)
      )

      choices.forEach { (level, label, icon) ->
        val isSelected = selectedRating == level

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedRating = level }
            .testTag("feedback_choice_${level.name.lowercase()}"),
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) ForestGreen else TextMuted,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = TextDark
              )
            }

            RadioButton(
              selected = isSelected,
              onClick = { selectedRating = level },
              colors = RadioButtonDefaults.colors(
                selectedColor = ForestGreen,
                unselectedColor = TextMuted
              )
            )
          }
        }
      }

      // Comments · Optional
      Column {
        Text(
          text = "Comments · Optional",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = comments,
          onValueChange = { comments = it },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("feedback_comments_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = OutlineBorder,
            focusedBorderColor = ForestGreen
          )
        )
      }

      // Submit Button
      Button(
        onClick = {
          onSubmitFeedback(selectedRating, comments)
          isSubmitted = true
          Toast.makeText(context, "Feedback recorded!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("submit_feedback_button"),
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
          text = "Submit Feedback",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      // AFTER SUBMITTING section (matching PDF page 13)
      Text(
        text = "AFTER SUBMITTING",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 1.sp
      )

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
              text = "Thank you — feedback received",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Your feedback has been added to this report. Its status remains In progress. You can still follow updates.",
              fontSize = 12.sp,
              color = TextMuted,
              lineHeight = 17.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
