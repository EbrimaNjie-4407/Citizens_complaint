package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InstitutionalRoutingRule
import com.example.ui.theme.*

@Composable
fun InstitutionalRoutingScreen(
  routingRules: List<InstitutionalRoutingRule>,
  onBackClick: () -> Unit,
  onUpdateRule: (InstitutionalRoutingRule) -> Unit,
  onResetDefaults: () -> Unit,
  onHelpClick: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var editingRule by remember { mutableStateOf<InstitutionalRoutingRule?>(null) }

  val filteredRules = remember(routingRules, searchQuery) {
    if (searchQuery.isBlank()) routingRules
    else routingRules.filter {
      it.categoryName.contains(searchQuery, ignoreCase = true) ||
        it.targetInstitutionName.contains(searchQuery, ignoreCase = true) ||
        (it.subcategoryFilter?.contains(searchQuery, ignoreCase = true) == true)
    }
  }

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(ForestGreenDark)
          .padding(horizontal = 12.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick, modifier = Modifier.testTag("routing_screen_back_button")) {
              Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "Institutional Routing Rules",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Configurable Dispatch & SLA Targets",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f)
              )
            }
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
      // Explanation card
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(Icons.Default.Tune, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
            Column {
              Text(
                text = "Dynamic Runtime Routing Engine",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Citizens filing complaints are automatically assigned to the designated authority according to these rules. Modify targets or SLA hours without changing any application code.",
                fontSize = 11.sp,
                color = Color(0xFFB45309),
                lineHeight = 16.sp
              )
            }
          }
        }
      }

      // Search & Reset Row
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter routing rules...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
            },
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("routing_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedContainerColor = Color.White,
              focusedContainerColor = Color.White,
              unfocusedBorderColor = OutlineBorder,
              focusedBorderColor = ForestGreen
            )
          )

          OutlinedButton(
            onClick = onResetDefaults,
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
          ) {
            Icon(Icons.Default.RestartAlt, contentDescription = null, tint = TextDark, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Reset", fontSize = 12.sp, color = TextDark)
          }
        }
      }

      // Rules Count
      item {
        Text(
          text = "${filteredRules.size} Configured Routing Rules",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = TextMuted
        )
      }

      // Rules List
      items(filteredRules) { rule ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { editingRule = rule }
            .testTag("routing_rule_${rule.categoryId}"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = rule.categoryName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEFF6FF)
              ) {
                Text(
                  text = "SLA: ${rule.prioritySlaHours}h",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1D4ED8),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AccountBalance, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Target Authority: ${rule.targetInstitutionName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Subcategory Scope: ${rule.subcategoryFilter ?: "All subcategories in this sector"}",
              fontSize = 11.sp,
              color = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Auto-escalate urgent: ${if (rule.autoEscalateUrgent) "Enabled" else "Disabled"}",
                fontSize = 10.sp,
                color = TextMuted
              )

              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Edit Rule", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                Spacer(modifier = Modifier.width(2.dp))
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(13.dp))
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

  // Edit Rule Modal Dialog
  editingRule?.let { rule ->
    var targetInstName by remember { mutableStateOf(rule.targetInstitutionName) }
    var slaHoursText by remember { mutableStateOf(rule.prioritySlaHours.toString()) }
    var autoEscalate by remember { mutableStateOf(rule.autoEscalateUrgent) }

    val institutions = listOf(
      "National Roads Authority (NRA)",
      "Kanifing Municipal Council (KMC)",
      "Brikama Area Council (BAC)",
      "Banjul City Council (BCC)",
      "NAWEC (Water & Electricity Company)",
      "PURA (Public Utilities Regulatory Authority)",
      "National Environment Agency (NEA)",
      "National Disaster Management Agency (NDMA)",
      "Ministry of Basic & Secondary Education (MoBSE)",
      "Ministry of Health (MoH)",
      "Gambia Ferry Services (GPA)",
      "The Gambia Fire & Rescue Services",
      "The Gambia Police Force",
      "Department of Social Welfare"
    )

    AlertDialog(
      onDismissRequest = { editingRule = null },
      title = { Text("Configure Routing Rule", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(text = "Category: ${rule.categoryName}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)

          Text("Assigned Public Authority:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)

          // Scrollable radio choices for institution
          institutions.take(6).forEach { inst ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { targetInstName = inst }
                .padding(vertical = 3.dp)
            ) {
              RadioButton(selected = targetInstName == inst, onClick = { targetInstName = inst })
              Spacer(modifier = Modifier.width(6.dp))
              Text(inst, fontSize = 11.sp, color = TextDark)
            }
          }

          OutlinedTextField(
            value = slaHoursText,
            onValueChange = { slaHoursText = it.filter { char -> char.isDigit() } },
            label = { Text("SLA Target Turnaround (Hours)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Checkbox(checked = autoEscalate, onCheckedChange = { autoEscalate = it })
            Spacer(modifier = Modifier.width(6.dp))
            Text("Auto-escalate critical incidents", fontSize = 12.sp, color = TextDark)
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val hours = slaHoursText.toIntOrNull() ?: 48
            val updated = rule.copy(
              targetInstitutionName = targetInstName,
              prioritySlaHours = hours,
              autoEscalateUrgent = autoEscalate
            )
            onUpdateRule(updated)
            editingRule = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Save Rule")
        }
      },
      dismissButton = {
        TextButton(onClick = { editingRule = null }) { Text("Cancel") }
      }
    )
  }
}
