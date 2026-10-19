package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Complaint
import com.example.data.model.ComplaintStatus
import com.example.ui.components.StatusBadge
import com.example.ui.components.VoiceNotePlayer
import com.example.ui.theme.*

@Composable
fun ReportDetailsScreen(
  complaint: Complaint,
  onBackClick: () -> Unit,
  onGiveFeedback: (String) -> Unit,
  onRequestReview: (String) -> Unit,
  onHelpClick: () -> Unit,
  onAdvanceFieldStep: ((String) -> Unit)? = null
) {
  var selectedPhotoTab by remember { mutableStateOf("Damage Site") }

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("report_details_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Report Details",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = complaint.id,
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
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Demonstration Control Banner matching Screenshot 2
      if (onAdvanceFieldStep != null) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F5FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBED3FE))
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
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = Icons.Default.AutoAwesome,
                  contentDescription = null,
                  tint = Color(0xFF1D4ED8),
                  modifier = Modifier.size(20.dp)
                )
                Column {
                  Text(
                    text = "Demonstration Control",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                  )
                  Text(
                    text = "Simulate field technician progress & auditory status alerts",
                    fontSize = 11.sp,
                    color = Color(0xFF3B82F6),
                    lineHeight = 14.sp
                  )
                }
              }

              Button(
                onClick = { onAdvanceFieldStep(complaint.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("simulate_field_step_button")
              ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Simulate Next Step", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // End-to-End Resolution Pipeline matching Screenshot 2
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "END-TO-END RESOLUTION PIPELINE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp
              )

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF)
              ) {
                Text(
                  text = "Stage ${complaint.currentStageNumber} of 6",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF2563EB),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Pipeline Stages Horizontal Scroll
            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(complaint.pipelineStages) { stage ->
                val isCompleted = stage.isCompleted
                val isActive = stage.isActive

                Card(
                  modifier = Modifier
                    .width(135.dp)
                    .height(115.dp),
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(
                    containerColor = when {
                      isActive -> Color(0xFFF0F7FF)
                      isCompleted -> Color.White
                      else -> BackgroundLight
                    }
                  ),
                  border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    when {
                      isActive -> Color(0xFF2563EB)
                      isCompleted -> Color(0xFF93C5FD)
                      else -> OutlineBorder
                    }
                  )
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxSize()
                      .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                  ) {
                    Box(
                      modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                          when {
                            isCompleted || isActive -> Color(0xFF2563EB)
                            else -> Color(0xFFCBD5E1)
                          }
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      if (isCompleted || isActive) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = null,
                          tint = Color.White,
                          modifier = Modifier.size(15.dp)
                        )
                      } else {
                        Text(
                          text = stage.stageNumber.toString(),
                          fontSize = 11.sp,
                          color = Color.White,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Text(
                        text = stage.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive || isCompleted) TextDark else TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        maxLines = 1
                      )
                      Text(
                        text = stage.subtext,
                        fontSize = 9.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        maxLines = 1
                      )
                    }

                    Text(
                      text = stage.time,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = if (isActive) Color(0xFF2563EB) else TextMuted
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Assigned Field Engineering Unit & Crew Telemetry matching Screenshot 2
      complaint.authorityMeasures?.let { measures ->
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.LocalShipping,
                  contentDescription = null,
                  tint = Color(0xFF2563EB),
                  modifier = Modifier.size(18.dp)
                )
                Text(
                  text = "ASSIGNED FIELD ENGINEERING UNIT & CREW TELEMETRY",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextMuted,
                  letterSpacing = 0.5.sp
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Unit Card
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = BackgroundLight
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                    text = "RAPID RESPONSE UNIT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = measures.rapidResponseUnit,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                  )
                  Text(
                    text = measures.liaisonUnit,
                    fontSize = 12.sp,
                    color = TextMuted
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Supervisor Card
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
                        text = "LEAD ENGINEER / SUPERVISOR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                      )
                      Text(
                        text = measures.leadSupervisor,
                        fontSize = 14.sp,
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

              // Arrival & Diagnostics Status Card
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = BackgroundLight
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                    text = "ARRIVAL & DIAGNOSTICS STATUS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
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
            }
          }
        }

        // MEASURES TAKEN BY THE RESPONSIBLE AUTHORITY
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f))
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                  Text(text = eq, fontSize = 12.sp, color = TextDark, lineHeight = 16.sp)
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "Remediation & Field Actions Completed:",
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
                    modifier = Modifier.size(14.dp).offset(y = 2.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(text = action, fontSize = 12.sp, color = TextDark, lineHeight = 17.sp)
                }
              }
            }
          }
        }
      }

      // Photographic Site Records (Damage Site vs Remediated Site) matching Screenshot 2
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "PHOTOGRAPHIC SITE RECORDS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
              )

              // Toggle Tabs matching Screenshot 2
              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .background(BackgroundLight)
                  .padding(2.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (selectedPhotoTab == "Damage Site") Color.White else Color.Transparent,
                  shadowElevation = if (selectedPhotoTab == "Damage Site") 1.dp else 0.dp,
                  modifier = Modifier.clickable { selectedPhotoTab = "Damage Site" }
                ) {
                  Text(
                    text = "Damage Site",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedPhotoTab == "Damage Site") TextDark else TextMuted,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (selectedPhotoTab == "Remediated Site") ForestGreen else Color.Transparent,
                  shadowElevation = if (selectedPhotoTab == "Remediated Site") 1.dp else 0.dp,
                  modifier = Modifier.clickable { selectedPhotoTab = "Remediated Site" }
                ) {
                  Text(
                    text = "Remediated Site",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedPhotoTab == "Remediated Site") Color.White else TextMuted,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedPhotoTab == "Remediated Site") {
              Image(
                painter = painterResource(id = R.drawable.remediated_waste_site_1792436266625),
                contentDescription = "Remediated and cleaned site",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(190.dp)
                  .clip(RoundedCornerShape(12.dp))
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Post-remediation inspection photo: Sidewalk cleared and sanitized with new wheelie receptacles.",
                fontSize = 11.sp,
                color = ForestGreen,
                fontWeight = FontWeight.Medium
              )
            } else {
              if (complaint.photoUri != null) {
                AsyncImage(
                  model = complaint.photoUri,
                  contentDescription = "Damage site photo",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp))
                )
              } else {
                Image(
                  painter = painterResource(
                    id = complaint.photoDrawableRes ?: R.drawable.waste_evidence_photo_1792407002003
                  ),
                  contentDescription = "Damage site photo",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp))
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Initial citizen damage evidence recorded during report submission.",
                fontSize = 11.sp,
                color = TextMuted
              )
            }
          }
        }
      }

      // Complaint header details card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = complaint.id,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen
              )

              StatusBadge(status = complaint.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = complaint.title,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "${complaint.locationName} · ${complaint.assignedInstitution ?: "Public Agency"}",
              fontSize = 13.sp,
              color = TextMuted
            )

            if (complaint.hasVoiceNote) {
              Spacer(modifier = Modifier.height(12.dp))
              VoiceNotePlayer(durationText = complaint.voiceDuration ?: "0:12")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = complaint.description,
              fontSize = 13.sp,
              color = TextDark,
              lineHeight = 19.sp
            )
          }
        }
      }

      // Institutional update quote card if available
      complaint.institutionalUpdate?.let { update ->
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreen.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Text(
                text = "Institutional update",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "“${update.quote}”",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = TextDark,
                lineHeight = 20.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = update.author,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen
              )
            }
          }
        }
      }

      // Actions Footer
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = { onGiveFeedback(complaint.id) },
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
          ) {
            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Give Feedback", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }

          OutlinedButton(
            onClick = { onRequestReview(complaint.id) },
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen)
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Request Review", color = ForestGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
