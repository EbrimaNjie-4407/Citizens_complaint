package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.*
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun AdminComplaintDetailScreen(
  complaint: Complaint,
  recurringHistory: List<Complaint>,
  userProfile: UserProfile,
  onBackClick: () -> Unit,
  onAssignInstitution: (institutionId: String, institutionName: String, officerName: String?) -> Unit,
  onEscalateSeverity: (newUrgency: UrgencyLevel, reason: String) -> Unit,
  onFlagDuplicate: (duplicateOfId: String) -> Unit,
  onPostInstitutionalUpdate: (quote: String, author: String) -> Unit,
  onResolveWithEvidence: (notes: String, actions: List<String>, equipment: List<String>) -> Unit,
  onAdvanceFieldStep: () -> Unit,
  onHelpClick: () -> Unit
) {
  var showReassignDialog by remember { mutableStateOf(false) }
  var showEscalateDialog by remember { mutableStateOf(false) }
  var showDuplicateDialog by remember { mutableStateOf(false) }
  var showUpdateDialog by remember { mutableStateOf(false) }
  var showResolveDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(ForestGreenDark)
          .padding(horizontal = 12.dp, vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("admin_detail_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "Case Management",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "${complaint.id} · ${complaint.assignedInstitution ?: "Triage"}",
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
      // Alert banners (Overdue or Duplicate)
      if (complaint.isOverdue) {
        item {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = UrgencyCritical.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, UrgencyCritical)
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = UrgencyCritical)
              Column {
                Text("OVERDUE INCIDENT ALERT (>48h SLA breached)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = UrgencyCritical)
                Text("This complaint exceeded standard civic turnaround response threshold. Immediate supervisor dispatch mandated.", fontSize = 11.sp, color = TextDark)
              }
            }
          }
        }
      }

      if (complaint.isDuplicate) {
        item {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFEF3C7),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD97706))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFFB45309))
              Column {
                Text("DUPLICATE INCIDENT DETECTED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                Text("Corresponds with primary incident reference ${complaint.duplicateOfId ?: "CG-2026-01482"} in ${complaint.community}.", fontSize = 11.sp, color = TextDark)
              }
            }
          }
        }
      }

      // Complaint Header Card
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
              Column {
                Text(text = complaint.id, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ForestGreenDark)
                Text(text = "Logged: ${complaint.createdAt}", fontSize = 11.sp, color = TextMuted)
              }
              StatusBadge(status = complaint.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = complaint.title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextDark)

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFEFF6FF)
            ) {
              Text(
                text = "${complaint.category.displayName} · Subcategory: ${complaint.subcategory ?: complaint.category.shortName}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D4ED8),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${complaint.locationName} · ${complaint.community}, ${complaint.district}",
                fontSize = 12.sp,
                color = TextDark
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = complaint.description,
              fontSize = 13.sp,
              color = TextMuted,
              lineHeight = 18.sp
            )

            complaint.priorityEscalation?.let { esc ->
              Spacer(modifier = Modifier.height(8.dp))
              Text(text = "⚡ $esc", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UrgencyCritical)
            }
          }
        }
      }

      // Quick Action Buttons Row (Assign, Escalate, Flag Duplicate, Update, Resolve)
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = "ADMINISTRATIVE WORKFLOW ACTIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { showReassignDialog = true },
              modifier = Modifier.weight(1f).height(44.dp),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
              Icon(Icons.Default.AssignmentInd, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Route / Assign", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { showEscalateDialog = true },
              modifier = Modifier.weight(1f).height(44.dp),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = UrgencyCritical)
            ) {
              Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Escalate", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = { showUpdateDialog = true },
              modifier = Modifier.weight(1f).height(44.dp),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen)
            ) {
              Icon(Icons.Default.Campaign, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Post Statement", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }

            OutlinedButton(
              onClick = { showDuplicateDialog = true },
              modifier = Modifier.weight(1f).height(44.dp),
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextDark, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Link Duplicate", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
          }

          // Complete Resolution Button
          Button(
            onClick = { showResolveDialog = true },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Resolve & File Verified Evidence", fontSize = 14.sp, fontWeight = FontWeight.Bold)
          }

          // Advance Next Pipeline Stage Shortcut
          OutlinedButton(
            onClick = onAdvanceFieldStep,
            modifier = Modifier.fillMaxWidth().height(42.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B82F6))
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Advance Pipeline Stage (${complaint.currentStageNumber} of 6)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
          }
        }
      }

      // Recurring Infrastructure History Card
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
              Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(20.dp))
              Text(
                text = "RECURRING INFRASTRUCTURE HISTORY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3A8A),
                letterSpacing = 0.5.sp
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Past public complaints logged in ${complaint.community} (${recurringHistory.size} total recorded):",
              fontSize = 12.sp,
              color = TextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (recurringHistory.isEmpty()) {
              Text("No recurring issues logged at this locality.", fontSize = 12.sp, fontStyle = FontStyle.Italic, color = TextMuted)
            } else {
              recurringHistory.take(4).forEach { past ->
                Surface(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  shape = RoundedCornerShape(8.dp),
                  color = BackgroundLight
                ) {
                  Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column(modifier = Modifier.weight(1f)) {
                      Text(text = past.id, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreenDark)
                      Text(text = past.title, fontSize = 11.sp, color = TextDark, maxLines = 1)
                      Text(text = "${past.subcategory ?: past.category.displayName} · ${past.createdAt.substringBefore(" ·")}", fontSize = 10.sp, color = TextMuted)
                    }
                    StatusBadge(status = past.status)
                  }
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Dialog: Reassign Institution
  if (showReassignDialog) {
    val institutions = listOf(
      "National Roads Authority (NRA)" to "nra",
      "Kanifing Municipal Council (KMC)" to "kmc",
      "NAWEC (Water & Electricity Company)" to "nawec",
      "PURA (Public Utilities Regulatory Authority)" to "pura",
      "National Environment Agency (NEA)" to "nea",
      "National Disaster Management Agency (NDMA)" to "ndma",
      "Ministry of Basic & Secondary Education (MoBSE)" to "mobse",
      "Ministry of Health (MoH)" to "moh",
      "Gambia Ferry Services (GPA)" to "gpa",
      "The Gambia Fire & Rescue Services" to "fire_service"
    )
    var selectedInst by remember { mutableStateOf(institutions.first()) }
    var officerInput by remember { mutableStateOf("Supervisor Modou Sanyang") }

    AlertDialog(
      onDismissRequest = { showReassignDialog = false },
      title = { Text("Route to Public Authority", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Select target Gambian authority and assign officer:", fontSize = 12.sp, color = TextMuted)
          institutions.take(5).forEach { inst ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedInst = inst }
                .padding(vertical = 4.dp)
            ) {
              RadioButton(selected = selectedInst == inst, onClick = { selectedInst = inst })
              Spacer(modifier = Modifier.width(6.dp))
              Text(inst.first, fontSize = 12.sp, color = TextDark)
            }
          }
          OutlinedTextField(
            value = officerInput,
            onValueChange = { officerInput = it },
            label = { Text("Designated Field Officer") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onAssignInstitution(selectedInst.second, selectedInst.first, officerInput.ifBlank { null })
            showReassignDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Confirm Routing")
        }
      },
      dismissButton = {
        TextButton(onClick = { showReassignDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Dialog: Escalate Severity
  if (showEscalateDialog) {
    var reasonInput by remember { mutableStateOf("Hazardous conditions threatening public safety and road passage.") }
    AlertDialog(
      onDismissRequest = { showEscalateDialog = false },
      title = { Text("Escalate Urgency Level", fontWeight = FontWeight.Bold, color = UrgencyCritical) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Escalate this complaint to CRITICAL status to trigger immediate rapid-response crew dispatch.", fontSize = 12.sp, color = TextMuted)
          OutlinedTextField(
            value = reasonInput,
            onValueChange = { reasonInput = it },
            label = { Text("Escalation Rationale") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onEscalateSeverity(UrgencyLevel.CRITICAL, reasonInput)
            showEscalateDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = UrgencyCritical)
        ) {
          Text("Escalate to CRITICAL")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEscalateDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Dialog: Duplicate Link
  if (showDuplicateDialog) {
    var primaryIdInput by remember { mutableStateOf("CG-2026-01482") }
    AlertDialog(
      onDismissRequest = { showDuplicateDialog = false },
      title = { Text("Flag Duplicate Report", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Link this report to an existing primary reference ID in the same community to prevent split crew dispatch:", fontSize = 12.sp, color = TextMuted)
          OutlinedTextField(
            value = primaryIdInput,
            onValueChange = { primaryIdInput = it },
            label = { Text("Primary Complaint ID") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onFlagDuplicate(primaryIdInput.trim())
            showDuplicateDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Link Duplicate")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDuplicateDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Dialog: Post Statement
  if (showUpdateDialog) {
    var quoteInput by remember { mutableStateOf("Field team has cordoned the area and replacement components are on site.") }
    AlertDialog(
      onDismissRequest = { showUpdateDialog = false },
      title = { Text("Post Official Statement", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("This statement will appear prominently to citizens tracking this complaint:", fontSize = 12.sp, color = TextMuted)
          OutlinedTextField(
            value = quoteInput,
            onValueChange = { quoteInput = it },
            label = { Text("Official Institutional Update") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val author = "${complaint.assignedInstitution ?: "Directorate"} · Officer Verification"
            onPostInstitutionalUpdate(quoteInput, author)
            showUpdateDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Post Update")
        }
      },
      dismissButton = {
        TextButton(onClick = { showUpdateDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Dialog: Resolve With Evidence
  if (showResolveDialog) {
    var notesInput by remember { mutableStateOf("Site remediation completed. Obstruction cleared and verified safe for public access.") }
    AlertDialog(
      onDismissRequest = { showResolveDialog = false },
      title = { Text("Resolve & File Evidence", fontWeight = FontWeight.Bold, color = ForestGreen) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Record supervisor verification notes and confirm corrective remediation:", fontSize = 12.sp, color = TextMuted)
          OutlinedTextField(
            value = notesInput,
            onValueChange = { notesInput = it },
            label = { Text("Verified Resolution Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val actions = listOf(
              "Physical corrective remediation applied on location.",
              "Post-repair structural inspection passed.",
              "Area decontaminated and safety cordon removed."
            )
            val equipment = listOf(
              "Municipal heavy duty response unit",
              "Certified diagnostic field equipment"
            )
            onResolveWithEvidence(notesInput, actions, equipment)
            showResolveDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Confirm Resolution")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResolveDialog = false }) { Text("Cancel") }
      }
    )
  }
}
