package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun TrackReportScreen(
  initialId: String = "CG-2026-01482",
  complaints: List<Complaint> = emptyList(),
  onBackClick: () -> Unit,
  onHelpClick: () -> Unit,
  onTrackSubmitted: (String) -> Unit,
  onOpenDetails: (String) -> Unit
) {
  var complaintIdInput by remember { mutableStateOf(initialId) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var activeTrackedId by remember { mutableStateOf(initialId) }

  // Look up complaint in provided list
  val trackedComplaint = complaints.find {
    it.id.equals(activeTrackedId.trim(), ignoreCase = true)
  } ?: complaints.firstOrNull()

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("track_report_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Track Report",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Open to everyone · No sign-in needed",
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
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header Description
      Column {
        Text(
          text = "See how your report is moving.",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Enter a complaint reference or tap a quick report below to see real-time measures taken by the responsible public authority.",
          fontSize = 13.sp,
          color = TextMuted,
          lineHeight = 18.sp
        )
      }

      // Input Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Complaint ID / reference",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = complaintIdInput,
            onValueChange = {
              complaintIdInput = it
              errorMessage = null
            },
            placeholder = { Text("e.g. CG-2026-01482") },
            leadingIcon = {
              Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(20.dp)
              )
            },
            trailingIcon = {
              if (complaintIdInput.isNotEmpty()) {
                IconButton(onClick = { complaintIdInput = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(18.dp))
                }
              }
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("track_complaint_id_input"),
            shape = RoundedCornerShape(12.dp),
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = ForestGreen,
              unfocusedBorderColor = OutlineBorder
            )
          )

          if (errorMessage != null) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = errorMessage ?: "",
              color = UrgencyCritical,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Quick selection chips for available complaints
          Text(
            text = "Quick Reports in The Gambia:",
            fontSize = 11.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.height(6.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(complaints) { c ->
              val isSelected = c.id.equals(activeTrackedId, ignoreCase = true)
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) ForestGreen else BackgroundLight,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) ForestGreen else OutlineBorder
                ),
                modifier = Modifier.clickable {
                  complaintIdInput = c.id
                  activeTrackedId = c.id
                  errorMessage = null
                }
              ) {
                Text(
                  text = "${c.id} (${c.category.shortName})",
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else TextDark,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = {
              val cleanId = complaintIdInput.trim()
              if (cleanId.isBlank()) {
                errorMessage = "Please enter a valid complaint ID"
              } else {
                activeTrackedId = cleanId
                val found = complaints.any { it.id.equals(cleanId, ignoreCase = true) }
                if (!found) {
                  errorMessage = "ID '$cleanId' not found. Select from the quick reports above."
                } else {
                  onTrackSubmitted(cleanId)
                }
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("submit_track_button"),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
          ) {
            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Track Report",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Detailed Measures Taken by the Responsible Authority Section
      if (trackedComplaint != null) {
        val measures = trackedComplaint.authorityMeasures

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("authority_measures_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.35f))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header: ID + Category + Status Badge
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = trackedComplaint.id,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = ForestGreen
                )
                Text(
                  text = trackedComplaint.createdAt,
                  fontSize = 11.sp,
                  color = TextMuted
                )
              }

              StatusBadge(status = trackedComplaint.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = trackedComplaint.title,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${trackedComplaint.locationName} · ${trackedComplaint.assignedInstitution ?: "Municipal Council"}",
                fontSize = 12.sp,
                color = TextMuted
              )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(14.dp))

            // RESPONSIBLE AUTHORITY & MEASURES TAKEN HEADER
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
              Text(
                text = "MEASURES TAKEN BY RESPONSIBLE AUTHORITY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen,
                letterSpacing = 0.5.sp
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Assigned Authority & Field Engineering Unit
            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFF0FDF4),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "RESPONSIBLE INSTITUTION & RAPID RESPONSE UNIT",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = ForestGreenDark,
                  letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = measures?.rapidResponseUnit ?: "${trackedComplaint.assignedInstitution ?: "Municipal Council"} Operational Unit",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
                Text(
                  text = measures?.liaisonUnit ?: "Direct Municipal Civic Liaison Office",
                  fontSize = 12.sp,
                  color = TextMuted
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Supervisor & Telemetry
            if (measures != null) {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = BackgroundLight
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Engineering,
                      contentDescription = null,
                      tint = ForestGreen,
                      modifier = Modifier.size(24.dp)
                    )
                    Column {
                      Text(
                        text = "LEAD SUPERVISOR / ENGINEER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                      )
                      Text(
                        text = measures.leadSupervisor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                      )
                    }
                  }

                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ForestGreenLight
                  ) {
                    Text(
                      text = measures.supervisorCredentials,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = ForestGreen,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Arrival & Diagnostics Status
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = BackgroundLight
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                    text = "FIELD STATUS & TELEMETRY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ForestGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = measures.diagnosticsStatus,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = TextDark
                    )
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = measures.dispatchedTime,
                    fontSize = 11.sp,
                    color = ForestGreen,
                    fontWeight = FontWeight.Medium
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Concrete Measures & Remediation Actions Completed
              Text(
                text = "Field Actions & Remediation Completed:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )

              Spacer(modifier = Modifier.height(6.dp))

              measures.actionsTaken.forEach { action ->
                Row(
                  modifier = Modifier.padding(vertical = 3.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(15.dp).offset(y = 2.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = action,
                    fontSize = 12.sp,
                    color = TextDark,
                    lineHeight = 17.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Municipal Equipment & Logistics Deployed
              Text(
                text = "Equipment & Municipal Logistics Deployed:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )

              Spacer(modifier = Modifier.height(6.dp))

              measures.equipmentDeployed.forEach { eq ->
                Row(
                  modifier = Modifier.padding(vertical = 2.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Text(text = "• ", fontSize = 14.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
                  Text(
                    text = eq,
                    fontSize = 12.sp,
                    color = TextDark,
                    lineHeight = 16.sp
                  )
                }
              }
            }

            // Institutional Update Quote
            trackedComplaint.institutionalUpdate?.let { update ->
              Spacer(modifier = Modifier.height(12.dp))
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEFF6FF),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                    text = "Official Update Quote:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8)
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "“${update.quote}”",
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    color = TextDark,
                    lineHeight = 18.sp
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = update.author,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1D4ED8)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action: View Full Timeline & Photographic Records
            Button(
              onClick = { onOpenDetails(trackedComplaint.id) },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("view_full_report_details_button"),
              shape = RoundedCornerShape(24.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
              Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "View Full Pipeline & Photo Evidence →",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Where do I find my ID callout
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
              text = "Where do I find my ID?",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Look on your submission confirmation, saved screenshot or report acknowledgement. The ID looks like CG-2026-01482.",
              fontSize = 12.sp,
              color = TextMuted,
              lineHeight = 17.sp
            )
          }
        }
      }

      OutlinedButton(
        onClick = onBackClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("back_to_home_button"),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
      ) {
        Text(
          text = "Back to Home",
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextDark
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
