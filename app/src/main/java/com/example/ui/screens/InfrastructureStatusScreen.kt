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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SectorCondition
import com.example.ui.theme.*

@Composable
fun InfrastructureStatusScreen(
  sectors: List<SectorCondition>,
  onBackClick: () -> Unit,
  onOpenReportForSector: (String) -> Unit
) {
  var selectedSectorForAnalytics by remember { mutableStateOf<SectorCondition?>(null) }

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
          IconButton(onClick = onBackClick, modifier = Modifier.testTag("infra_status_back_button")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
          }

          Spacer(modifier = Modifier.width(8.dp))

          Column {
            Text(
              text = "Infrastructure Status",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "National Condition Matrix · The Gambia",
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
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header Section matching Screenshot 1
      item {
        Column {
          Text(
            text = "National Infrastructure Condition Matrix",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Simulated operational indicators, service uptime, and repair turnaround metrics across The Gambia.",
            fontSize = 13.sp,
            color = TextMuted,
            lineHeight = 18.sp
          )
        }
      }

      // Tip Card matching Screenshot 1
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD6E4FF))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "💡", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Tip: Click any sector below to inspect simulated utility trends and sub-station grids",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF1D4ED8),
              lineHeight = 16.sp
            )
          }
        }
      }

      // Sector Cards List matching Screenshot 1
      items(sectors) { sector ->
        val sectorIcon: ImageVector = when (sector.id) {
          "electricity" -> Icons.Default.Bolt
          "water" -> Icons.Default.WaterDrop
          "roads" -> Icons.Default.DirectionsCar
          "waste" -> Icons.Default.DeleteOutline
          else -> Icons.Default.CellTower
        }

        val primaryColor = when (sector.id) {
          "electricity" -> Color(0xFF2563EB)
          "water" -> Color(0xFF0284C7)
          "roads" -> Color(0xFF475569)
          "waste" -> ForestGreen
          else -> Color(0xFF7C3AED)
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedSectorForAnalytics = sector }
            .testTag("sector_card_${sector.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Icon + Title + Directorate + Condition Pill
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.12f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = sectorIcon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(24.dp)
                  )
                }

                Column {
                  Text(
                    text = sector.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                  )
                  Text(
                    text = sector.directorate,
                    fontSize = 12.sp,
                    color = TextMuted
                  )
                }
              }

              Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF93C5FD))
              ) {
                Text(
                  text = "Condition (${sector.conditionPercent}%)",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1D4ED8),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
              progress = { sector.conditionPercent / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = Color(0xFF2563EB),
              trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3-Column Metric Box matching Screenshot 1
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(BackgroundLight)
                .padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              // Col 1
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = sector.outputLabel,
                  fontSize = 11.sp,
                  color = TextMuted,
                  fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = sector.outputValue,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
              }

              // Col 2
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = sector.networkLabel,
                  fontSize = 11.sp,
                  color = TextMuted,
                  fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = sector.networkValue,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextDark
                )
              }

              // Col 3
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = sector.responseLabel,
                  fontSize = 11.sp,
                  color = TextMuted,
                  fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = sector.responseValue,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF2563EB)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Links Row matching Screenshot 1
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { selectedSectorForAnalytics = sector }
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                  contentDescription = null,
                  tint = Color(0xFF2563EB),
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "Click to View Graphs & Analytics →",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF2563EB)
                )
              }

              Text(
                text = "${sector.openReportsCount} open reports",
                fontSize = 11.sp,
                color = TextMuted
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // Analytics & Sub-stations Dialog
  selectedSectorForAnalytics?.let { sector ->
    AlertDialog(
      onDismissRequest = { selectedSectorForAnalytics = null },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Analytics, contentDescription = null, tint = ForestGreen)
          Text(text = "${sector.name} Operational Diagnostics", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = "Directorate: ${sector.directorate}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextDark
          )

          Text(
            text = "National Grid & Sub-station Health Breakdown:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
          )

          sector.subStations.forEach { (station, status) ->
            Surface(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(8.dp),
              color = BackgroundLight
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = station, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextDark)
                Text(text = status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val sId = sector.id
            selectedSectorForAnalytics = null
            onOpenReportForSector(sId)
          },
          colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
        ) {
          Text("Report Incident in this Sector", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { selectedSectorForAnalytics = null }) {
          Text("Close", color = TextDark)
        }
      }
    )
  }
}
