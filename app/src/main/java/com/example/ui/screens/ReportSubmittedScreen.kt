package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComplaintStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun ReportSubmittedScreen(
  complaintId: String,
  submissionDate: String,
  onTrackReport: (String) -> Unit,
  onReturnHome: () -> Unit,
  onHelpClick: () -> Unit
) {
  val context = LocalContext.current

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
          IconButton(onClick = onReturnHome) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Report Submitted",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Thank you for speaking up",
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
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(20.dp))

      // Checkmark icon circle
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(ForestGreenLight),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .border(2.dp, ForestGreen, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Success",
            tint = ForestGreen,
            modifier = Modifier.size(32.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "Your voice has been heard.",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Your report was submitted successfully. You can follow its progress with the ID below.",
        fontSize = 14.sp,
        color = TextMuted,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Complaint ID Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "COMPLAINT ID / REFERENCE",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 0.5.sp
            )

            StatusBadge(status = ComplaintStatus.RECEIVED)
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = complaintId,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenDark
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = submissionDate,
            fontSize = 13.sp,
            color = TextMuted
          )

          Spacer(modifier = Modifier.height(16.dp))

          OutlinedButton(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Citizen Gambia Reference ID", complaintId)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Complaint ID copied to clipboard!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("copy_save_id_button"),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
          ) {
            Icon(
              imageVector = Icons.Outlined.ContentCopy,
              contentDescription = null,
              tint = TextDark,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Copy / save ID",
              color = TextDark,
              fontWeight = FontWeight.SemiBold,
              fontSize = 14.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Track Report Button
      Button(
        onClick = { onTrackReport(complaintId) },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("track_report_button"),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
      ) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Track Report",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Return Home Button
      OutlinedButton(
        onClick = onReturnHome,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("return_home_button"),
        shape = RoundedCornerShape(25.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
      ) {
        Icon(
          imageVector = Icons.Outlined.Home,
          contentDescription = null,
          tint = TextDark,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Return Home",
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextDark
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
