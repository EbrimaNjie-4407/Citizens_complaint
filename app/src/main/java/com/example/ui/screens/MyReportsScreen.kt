package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.data.model.ComplaintStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

enum class ComplaintFilter(val label: String) {
  ALL("All"),
  RECEIVED("Received"),
  IN_PROGRESS("In progress"),
  RESOLVED("Resolved")
}

@Composable
fun MyReportsScreen(
  complaints: List<Complaint>,
  onBackClick: () -> Unit,
  onComplaintClick: (String) -> Unit,
  onTrackByIdClick: () -> Unit,
  onHelpClick: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedFilter by remember { mutableStateOf(ComplaintFilter.ALL) }

  val filteredComplaints = remember(complaints, searchQuery, selectedFilter) {
    complaints.filter { item ->
      val matchesSearch = searchQuery.isBlank() ||
        item.id.contains(searchQuery, ignoreCase = true) ||
        item.title.contains(searchQuery, ignoreCase = true) ||
        item.locationName.contains(searchQuery, ignoreCase = true) ||
        item.category.displayName.contains(searchQuery, ignoreCase = true)

      val matchesFilter = when (selectedFilter) {
        ComplaintFilter.ALL -> true
        ComplaintFilter.RECEIVED -> item.status == ComplaintStatus.RECEIVED
        ComplaintFilter.IN_PROGRESS -> item.status == ComplaintStatus.IN_PROGRESS || item.status == ComplaintStatus.ASSIGNED
        ComplaintFilter.RESOLVED -> item.status == ComplaintStatus.RESOLVED
      }

      matchesSearch && matchesFilter
    }
  }

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("my_reports_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "My Reports",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Your reports, all in one place",
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
      // Find a report header & Search Bar
      item {
        Text(
          text = "Find a report",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search by ID, issue or location") },
          leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = TextMuted)
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
              }
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_reports_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = OutlineBorder,
            focusedBorderColor = ForestGreen
          )
        )
      }

      // Filter Chips Row
      item {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          item {
            FilterChip(
              selected = selectedFilter == ComplaintFilter.ALL,
              onClick = { selectedFilter = ComplaintFilter.ALL },
              label = {
                Text("All ${complaints.size}", fontWeight = FontWeight.SemiBold)
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = Color.White,
                labelColor = TextDark
              ),
              border = if (selectedFilter == ComplaintFilter.ALL) null else FilterChipDefaults.filterChipBorder(
                borderColor = OutlineBorder,
                enabled = true,
                selected = false
              ),
              shape = RoundedCornerShape(20.dp)
            )
          }

          item {
            FilterChip(
              selected = selectedFilter == ComplaintFilter.RECEIVED,
              onClick = { selectedFilter = ComplaintFilter.RECEIVED },
              label = { Text("Received", fontWeight = FontWeight.SemiBold) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = Color.White,
                labelColor = TextDark
              ),
              border = if (selectedFilter == ComplaintFilter.RECEIVED) null else FilterChipDefaults.filterChipBorder(
                borderColor = OutlineBorder,
                enabled = true,
                selected = false
              ),
              shape = RoundedCornerShape(20.dp)
            )
          }

          item {
            FilterChip(
              selected = selectedFilter == ComplaintFilter.IN_PROGRESS,
              onClick = { selectedFilter = ComplaintFilter.IN_PROGRESS },
              label = { Text("In progress", fontWeight = FontWeight.SemiBold) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = Color.White,
                labelColor = TextDark
              ),
              border = if (selectedFilter == ComplaintFilter.IN_PROGRESS) null else FilterChipDefaults.filterChipBorder(
                borderColor = OutlineBorder,
                enabled = true,
                selected = false
              ),
              shape = RoundedCornerShape(20.dp)
            )
          }

          item {
            FilterChip(
              selected = selectedFilter == ComplaintFilter.RESOLVED,
              onClick = { selectedFilter = ComplaintFilter.RESOLVED },
              label = { Text("Resolved", fontWeight = FontWeight.SemiBold) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ForestGreen,
                selectedLabelColor = Color.White,
                containerColor = Color.White,
                labelColor = TextDark
              ),
              border = if (selectedFilter == ComplaintFilter.RESOLVED) null else FilterChipDefaults.filterChipBorder(
                borderColor = OutlineBorder,
                enabled = true,
                selected = false
              ),
              shape = RoundedCornerShape(20.dp)
            )
          }
        }
      }

      // Complaints List
      if (filteredComplaints.isEmpty()) {
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "No matching reports",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
              Text(
                text = "Try adjusting your search terms or filter selection.",
                fontSize = 13.sp,
                color = TextMuted
              )
            }
          }
        }
      } else {
        items(filteredComplaints) { item ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onComplaintClick(item.id) }
              .testTag("report_card_${item.id}"),
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
                  text = item.id,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = ForestGreen
                )

                StatusBadge(status = item.status)
              }

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = "${item.category.displayName} · ${item.assignedInstitution ?: "Municipal Council"}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )

              Spacer(modifier = Modifier.height(6.dp))

              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = TextMuted,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = item.locationName,
                  fontSize = 13.sp,
                  color = TextMuted
                )
              }

              Spacer(modifier = Modifier.height(10.dp))
              HorizontalDivider(color = DividerColor)
              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = item.createdAt.substringBefore(" ·"),
                  fontSize = 12.sp,
                  color = TextMuted
                )

                Text(
                  text = "View details →",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ForestGreen
                )
              }
            }
          }
        }
      }

      // Reported as a guest banner (PDF page 9)
      item {
        Spacer(modifier = Modifier.height(12.dp))

        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text(
              text = "Reported as a guest?",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Use your saved complaint ID. No account is needed to track it.",
              fontSize = 13.sp,
              color = TextMuted,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
              onClick = onTrackByIdClick,
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("track_by_id_guest_button"),
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
            ) {
              Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Track by ID",
                color = ForestGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
            }
          }
        }
      }
    }
  }
}
