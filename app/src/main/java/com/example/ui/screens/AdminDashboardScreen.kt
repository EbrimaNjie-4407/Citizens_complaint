package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.model.*
import com.example.data.repository.ComplaintRepository
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
  userProfile: UserProfile,
  complaints: List<Complaint>,
  kpiStats: ComplaintRepository.AdminKPIStats,
  onBackClick: () -> Unit,
  onSelectComplaint: (String) -> Unit,
  onOpenRoutingRules: () -> Unit,
  onSwitchRole: (UserRole) -> Unit,
  onHelpClick: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  val filters = listOf("All", "Overdue (>48h)", "My Institution", "Duplicates", "High/Critical", "Resolved")
  var selectedFilter by remember { mutableStateOf("All") }
  var showRoleSwitcherDialog by remember { mutableStateOf(false) }

  val filteredComplaints = remember(complaints, searchQuery, selectedFilter, userProfile) {
    complaints.filter { c ->
      val matchesSearch = searchQuery.isBlank() ||
        c.id.contains(searchQuery, ignoreCase = true) ||
        c.title.contains(searchQuery, ignoreCase = true) ||
        c.locationName.contains(searchQuery, ignoreCase = true) ||
        c.community.contains(searchQuery, ignoreCase = true) ||
        (c.subcategory?.contains(searchQuery, ignoreCase = true) == true)

      val matchesFilter = when (selectedFilter) {
        "Overdue (>48h)" -> c.isOverdue
        "Duplicates" -> c.isDuplicate
        "My Institution" -> c.assignedInstitutionId.equals(userProfile.officerInstitutionId, ignoreCase = true) ||
          c.assignedInstitution?.contains(userProfile.officerInstitutionId, ignoreCase = true) == true
        "High/Critical" -> c.urgency == UrgencyLevel.HIGH || c.urgency == UrgencyLevel.CRITICAL
        "Resolved" -> c.status == ComplaintStatus.RESOLVED
        else -> true
      }

      matchesSearch && matchesFilter
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
            IconButton(onClick = onBackClick, modifier = Modifier.testTag("admin_dashboard_back_button")) {
              Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Command Portal",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = if (userProfile.role == UserRole.SUPER_ADMIN) Color(0xFFD97706) else ForestGreen
                ) {
                  Text(
                    text = userProfile.role.displayName.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = if (userProfile.role == UserRole.SUPER_ADMIN) "National Oversight · All 22 Sectors" else userProfile.officerInstitutionName,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 1
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = { showRoleSwitcherDialog = true },
              modifier = Modifier.testTag("switch_role_button")
            ) {
              Icon(Icons.Default.SwitchAccount, contentDescription = "Switch Role", tint = Color.White)
            }
            IconButton(onClick = onHelpClick) {
              Icon(Icons.Default.HelpOutline, contentDescription = "Help", tint = Color.White)
            }
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
      // Role Switcher Callout Banner
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
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
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = ForestGreenDark, modifier = Modifier.size(22.dp))
              Column {
                Text(
                  text = "Active Role: ${userProfile.role.displayName}",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
                Text(
                  text = if (userProfile.role == UserRole.SUPER_ADMIN) "Full routing rules & cross-institutional powers" else "Assigned: ${userProfile.officerInstitutionName}",
                  fontSize = 11.sp,
                  color = TextMuted
                )
              }
            }

            TextButton(
              onClick = { showRoleSwitcherDialog = true },
              modifier = Modifier.testTag("toggle_role_link")
            ) {
              Text("Switch Role", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }
          }
        }
      }

      // Super-Admin Configurable Routing Rules Shortcut
      if (userProfile.role == UserRole.SUPER_ADMIN) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onOpenRoutingRules() }
              .testTag("open_routing_rules_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD97706)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Tune, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Column {
                  Text(
                    text = "Configurable Institutional Routing Rules",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E)
                  )
                  Text(
                    text = "Manage targets and SLA response times for all 22 categories",
                    fontSize = 11.sp,
                    color = Color(0xFFB45309)
                  )
                }
              }

              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF92400E),
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // KPI Metric Cards Grid
      item {
        Column {
          Text(
            text = "Institutional Performance Metrics",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Total
            KpiCard(
              title = "Total Cases",
              value = "${kpiStats.totalReports}",
              subtitle = "Logged in system",
              accentColor = Color(0xFF1E293B),
              modifier = Modifier.weight(1f)
            )

            // Overdue Alert
            KpiCard(
              title = "Overdue (>48h)",
              value = "${kpiStats.overdueCount}",
              subtitle = "SLA breached",
              accentColor = UrgencyCritical,
              modifier = Modifier.weight(1f)
            )

            // In Progress
            KpiCard(
              title = "In Progress",
              value = "${kpiStats.inProgress}",
              subtitle = "Active crews",
              accentColor = Color(0xFF2563EB),
              modifier = Modifier.weight(1f)
            )

            // Resolved
            KpiCard(
              title = "Resolved",
              value = "${kpiStats.resolved}",
              subtitle = "${kpiStats.resolutionRatePercent}% rate",
              accentColor = ForestGreen,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // Search & Filters
      item {
        Column {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by reference, community, category, officer...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                }
              }
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              unfocusedContainerColor = Color.White,
              focusedContainerColor = Color.White,
              unfocusedBorderColor = OutlineBorder,
              focusedBorderColor = ForestGreen
            )
          )

          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(filters) { filter ->
              val isSelected = selectedFilter == filter
              Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (isSelected) ForestGreenDark else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) ForestGreenDark else OutlineBorder
                ),
                modifier = Modifier
                  .clickable { selectedFilter = filter }
                  .testTag("admin_filter_${filter.take(4).lowercase()}")
              ) {
                Text(
                  text = filter,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else TextDark,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }
            }
          }
        }
      }

      // Complaints List
      if (filteredComplaints.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(36.dp))
              Spacer(modifier = Modifier.height(8.dp))
              Text("No matching cases found", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
              Text("All complaints in this queue are addressed.", fontSize = 12.sp, color = TextMuted)
            }
          }
        }
      } else {
        items(filteredComplaints) { complaint ->
          AdminComplaintCard(
            complaint = complaint,
            onClick = { onSelectComplaint(complaint.id) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Role Switcher Modal Dialog
  if (showRoleSwitcherDialog) {
    AlertDialog(
      onDismissRequest = { showRoleSwitcherDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(Icons.Default.Security, contentDescription = null, tint = ForestGreen)
          Text("Switch Interface Role", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Select a perspective to test role-appropriate workflows:",
            fontSize = 12.sp,
            color = TextMuted
          )

          // Citizen Option
          RoleSelectRow(
            title = "Citizen",
            description = "Submit complaints, track ID, review & rate",
            isSelected = userProfile.role == UserRole.CITIZEN,
            onClick = {
              onSwitchRole(UserRole.CITIZEN)
              showRoleSwitcherDialog = false
            }
          )

          // Institutional Officer Option
          RoleSelectRow(
            title = "Institutional Officer (KMC / NRA)",
            description = "Assign crews, telemetry, triage & resolve with evidence",
            isSelected = userProfile.role == UserRole.INSTITUTION_OFFICER,
            onClick = {
              onSwitchRole(UserRole.INSTITUTION_OFFICER)
              showRoleSwitcherDialog = false
            }
          )

          // Super-Admin Option
          RoleSelectRow(
            title = "Super-Admin",
            description = "National routing rules, SLA targets & cross-sector command",
            isSelected = userProfile.role == UserRole.SUPER_ADMIN,
            onClick = {
              onSwitchRole(UserRole.SUPER_ADMIN)
              showRoleSwitcherDialog = false
            }
          )
        }
      },
      confirmButton = {},
      dismissButton = {
        TextButton(onClick = { showRoleSwitcherDialog = false }) {
          Text("Cancel", color = TextDark)
        }
      }
    )
  }
}

@Composable
private fun KpiCard(
  title: String,
  value: String,
  subtitle: String,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(text = title, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium, maxLines = 1)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = accentColor)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = subtitle, fontSize = 9.sp, color = TextMuted, maxLines = 1)
    }
  }
}

@Composable
private fun AdminComplaintCard(
  complaint: Complaint,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("admin_complaint_${complaint.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    border = androidx.compose.foundation.BorderStroke(
      if (complaint.isOverdue) 1.5.dp else 1.dp,
      if (complaint.isOverdue) UrgencyCritical.copy(alpha = 0.6f) else OutlineBorder
    )
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top Row: Reference + Status + Badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = complaint.id,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenDark
          )

          if (complaint.isOverdue) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = UrgencyCritical.copy(alpha = 0.12f)
            ) {
              Text(
                text = "OVERDUE (>48H)",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = UrgencyCritical,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          if (complaint.isDuplicate) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFFEF3C7)
            ) {
              Text(
                text = "DUPLICATE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        StatusBadge(status = complaint.status)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = complaint.title,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Subcategory: ${complaint.subcategory ?: complaint.category.displayName}",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF2563EB)
      )

      Spacer(modifier = Modifier.height(2.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${complaint.locationName} · ${complaint.community}, ${complaint.district}",
          fontSize = 11.sp,
          color = TextMuted,
          maxLines = 1
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
      HorizontalDivider(color = DividerColor)
      Spacer(modifier = Modifier.height(8.dp))

      // Bottom Row: Assigned Officer / Institution & Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Assigned: ${complaint.assignedInstitution ?: "Unassigned"}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark
          )
          complaint.assignedOfficer?.let {
            Text(text = "Officer: $it", fontSize = 10.sp, color = TextMuted)
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Manage Case",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreen
          )
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun RoleSelectRow(
  title: String,
  description: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(10.dp),
    color = if (isSelected) ForestGreenLight else BackgroundLight,
    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ForestGreen else OutlineBorder)
  ) {
    Row(
      modifier = Modifier.padding(10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text(text = description, fontSize = 11.sp, color = TextMuted)
      }
      if (isSelected) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(18.dp))
      }
    }
  }
}
