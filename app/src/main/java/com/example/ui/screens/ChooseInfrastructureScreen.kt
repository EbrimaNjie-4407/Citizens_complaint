package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.model.Infrastructure
import com.example.data.model.InfrastructureCategory
import com.example.data.model.InfrastructureCategoryRegistry
import com.example.ui.theme.*

@Composable
fun ChooseInfrastructureScreen(
  infrastructures: List<Infrastructure>,
  initialFilter: String = "All",
  onBackClick: () -> Unit,
  onSelectInfrastructure: (Infrastructure) -> Unit,
  onSelectCategorySubcategory: (category: InfrastructureCategory, subcategory: String?) -> Unit = { _, _ -> }
) {
  var searchQuery by remember { mutableStateOf("") }
  val groups = listOf(
    "All",
    "Transport & Streets",
    "Sanitation & Water",
    "Utilities & Energy",
    "Civic & Public Facilities",
    "Environment & Natural Hazards",
    "Safety & Emergency",
    "Ports, Maritime & Aviation",
    "Rural & Primary Industries"
  )
  var selectedGroup by remember { mutableStateOf(initialFilter) }
  var expandedCategoryId by remember { mutableStateOf<String?>(null) }

  val filteredCategories = remember(searchQuery, selectedGroup) {
    InfrastructureCategoryRegistry.allCategories.filter { cat ->
      val matchesGroup = selectedGroup == "All" || cat.groupName.equals(selectedGroup, ignoreCase = true)
      val matchesSearch = searchQuery.isBlank() ||
        cat.name.contains(searchQuery, ignoreCase = true) ||
        cat.groupName.contains(searchQuery, ignoreCase = true) ||
        cat.defaultInstitutionName.contains(searchQuery, ignoreCase = true) ||
        cat.subcategories.any { it.contains(searchQuery, ignoreCase = true) }
      matchesGroup && matchesSearch
    }
  }

  Scaffold(
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(ForestGreen)
          .padding(horizontal = 12.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("choose_infra_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Spacer(modifier = Modifier.width(8.dp))

          Column {
            Text(
              text = "Infrastructure Categories",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "22 Public Sectors · The Gambia",
              fontSize = 12.sp,
              color = Color.White.copy(alpha = 0.85f)
            )
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
      // Search Box
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search 22 categories & subcategories (e.g. potholes, burst pipes, school toilets)...", fontSize = 13.sp) },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(18.dp))
              }
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("infra_search_input"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = OutlineBorder,
            focusedBorderColor = ForestGreen
          )
        )
      }

      // Quick Group Filter Horizontal Chips
      item {
        Column {
          Text(
            text = "Group Filter",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(groups) { group ->
              val isSelected = selectedGroup.equals(group, ignoreCase = true)
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) ForestGreen else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (isSelected) ForestGreen else OutlineBorder
                ),
                modifier = Modifier
                  .clickable { selectedGroup = group }
                  .testTag("group_chip_${group.take(6).lowercase()}")
              ) {
                Text(
                  text = group,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else TextDark,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
              }
            }
          }
        }
      }

      // Results Count & Informational Note
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${filteredCategories.size} Categories Available",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
          )
          Text(
            text = "Tap subcategory to report",
            fontSize = 11.sp,
            color = ForestGreen,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Categories List (All 22 Categories)
      items(filteredCategories) { category ->
        val isExpanded = expandedCategoryId == category.id
        val categoryColor = Color(category.colorHex)

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { expandedCategoryId = if (isExpanded) null else category.id }
            .testTag("category_item_${category.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          border = androidx.compose.foundation.BorderStroke(
            if (isExpanded) 1.5.dp else 1.dp,
            if (isExpanded) categoryColor else OutlineBorder
          )
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(categoryColor.copy(alpha = 0.12f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = when (category.id) {
                      "roads_streets" -> Icons.Default.AltRoute
                      "public_transport_safety" -> Icons.Default.DirectionsBus
                      "drainage_flooding" -> Icons.Default.Waves
                      "waste_management" -> Icons.Default.DeleteOutline
                      "water_sewerage" -> Icons.Default.WaterDrop
                      "electricity" -> Icons.Default.Bolt
                      "telecom" -> Icons.Default.CellTower
                      "education" -> Icons.Default.School
                      "healthcare" -> Icons.Default.LocalHospital
                      "markets_buildings" -> Icons.Default.Storefront
                      "parks_recreation" -> Icons.Default.Park
                      "coastal_protection" -> Icons.Default.Sailing
                      "environmental_hazards" -> Icons.Default.Forest
                      "emergency_infrastructure" -> Icons.Default.FireHydrantAlt
                      "public_safety" -> Icons.Default.Shield
                      "disaster_preparedness" -> Icons.Default.WarningAmber
                      "ferries_river" -> Icons.Default.DirectionsBoat
                      "ports_aviation" -> Icons.Default.FlightTakeoff
                      "accessibility" -> Icons.Default.Accessible
                      "rural_infrastructure" -> Icons.Default.Terrain
                      "agriculture" -> Icons.Default.Agriculture
                      else -> Icons.Default.Phishing
                    },
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Column {
                  Text(
                    text = category.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                  )
                  Text(
                    text = "${category.groupName} · ${category.defaultInstitutionName}",
                    fontSize = 11.sp,
                    color = TextMuted,
                    maxLines = 1
                  )
                }
              }

              Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(22.dp)
              )
            }

            // Expandable Subcategories Chips
            AnimatedVisibility(visible = isExpanded || searchQuery.isNotBlank()) {
              Column(modifier = Modifier.padding(top = 12.dp)) {
                HorizontalDivider(color = DividerColor)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = "Subcategories (select to file targeted report):",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subcategories Flow
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                  category.subcategories.chunked(2).forEach { rowSubcats ->
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      rowSubcats.forEach { subcat ->
                        Surface(
                          modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectCategorySubcategory(category, subcat) },
                          shape = RoundedCornerShape(10.dp),
                          color = categoryColor.copy(alpha = 0.08f),
                          border = androidx.compose.foundation.BorderStroke(1.dp, categoryColor.copy(alpha = 0.25f))
                        ) {
                          Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                          ) {
                            Text(
                              text = subcat,
                              fontSize = 11.sp,
                              fontWeight = FontWeight.Medium,
                              color = TextDark,
                              modifier = Modifier.weight(1f),
                              maxLines = 1
                            )
                            Icon(
                              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                              contentDescription = null,
                              tint = categoryColor,
                              modifier = Modifier.size(12.dp)
                            )
                          }
                        }
                      }
                      if (rowSubcats.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                  onClick = { onSelectCategorySubcategory(category, null) },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                  shape = RoundedCornerShape(20.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = categoryColor)
                ) {
                  Text(
                    text = "Report General ${category.name} Issue",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }

      // Gambian Institutions Direct Contacts Section (Preserved from Figma/PDF)
      item {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Key Public Authority Contacts",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Direct civic liaisons across public agencies in The Gambia.",
          fontSize = 12.sp,
          color = TextMuted
        )
      }

      items(infrastructures) { infra ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectInfrastructure(infra) }
            .testTag("infra_contact_${infra.id}"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
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
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(infra.logoBadgeColor)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = infra.name.take(2).uppercase(),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(infra.logoTextColor)
                )
              }

              Column {
                Text(
                  text = infra.fullName,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
                Text(
                  text = "${infra.categoryTag} · Hotline: ${infra.emergencyPhone}",
                  fontSize = 11.sp,
                  color = ForestGreen,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(16.dp),
              color = ForestGreenLight
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(infra.emergencyPhone, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
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
}
