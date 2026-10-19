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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Complaint
import com.example.data.model.IssueCategory
import com.example.data.model.UserProfile
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

data class CategoryUiItem(
  val category: IssueCategory?,
  val label: String,
  val icon: ImageVector,
  val iconColor: Color,
  val isMore: Boolean = false
)

@Composable
fun HomeScreen(
  userProfile: UserProfile,
  recentComplaints: List<Complaint>,
  onReportIssue: () -> Unit,
  onTrackReportShortcut: () -> Unit,
  onCategorySelected: (IssueCategory) -> Unit,
  onMoreCategories: () -> Unit,
  onComplaintClick: (String) -> Unit,
  onSeeAllComplaints: () -> Unit,
  onAlertBannerClick: (String) -> Unit,
  onOpenInfrastructureStatus: () -> Unit,
  onOpenEmergencyContacts: () -> Unit,
  modifier: Modifier = Modifier
) {
  val categories = listOf(
    CategoryUiItem(IssueCategory.WATER, "Water", Icons.Default.WaterDrop, Color(0xFF0288D1)),
    CategoryUiItem(IssueCategory.ELECTRICITY, "Electricity", Icons.Default.Bolt, Color(0xFFF57C00)),
    CategoryUiItem(IssueCategory.ROADS, "Roads", Icons.Default.AltRoute, Color(0xFF5C6BC0)),
    CategoryUiItem(IssueCategory.GSM, "GSM", Icons.Default.CellTower, Color(0xFFAB47BC)),
    CategoryUiItem(IssueCategory.WASTE, "Waste", Icons.Default.DeleteOutline, Color(0xFFE65100)),
    CategoryUiItem(IssueCategory.HEALTH, "Health", Icons.Default.FavoriteBorder, Color(0xFFE53935)),
    CategoryUiItem(IssueCategory.SECURITY, "Security", Icons.Default.Shield, Color(0xFF1E88E5)),
    CategoryUiItem(null, "More", Icons.Default.Apps, Color(0xFF78909C), isMore = true)
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // Top Green Curved Header
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
          .background(ForestGreen)
          .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Good morning 👋",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
              )
              Text(
                text = if (userProfile.isGuest) "Hello, Citizen!" else "Hello, ${userProfile.fullName}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Citizen Gambia · Your voice, your city",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // "Have an issue to report?" Card
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onReportIssue() }
              .testTag("home_report_issue_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ForestGreenDark.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = "Have an issue to report?",
                  fontSize = 13.sp,
                  color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Report an Issue",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .clickable { onTrackReportShortcut() }
                    .padding(vertical = 2.dp)
                    .testTag("home_track_shortcut")
                ) {
                  Icon(
                    imageVector = Icons.Default.LocationSearching,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Track a report →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                  )
                }
              }

              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "New Report",
                  tint = Color.White,
                  modifier = Modifier.size(28.dp)
                )
              }
            }
          }
        }
      }
    }

    // Sub-navigation Pills Row matching Screenshot 1 (Home, Infrastructure Status, My Reports, Emergency Contacts)
    item {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = ForestGreen,
            modifier = Modifier.testTag("nav_pill_home")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Home", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        item {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFEFF6FF),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF93C5FD)),
            modifier = Modifier
              .clickable { onOpenInfrastructureStatus() }
              .testTag("nav_pill_infrastructure_status")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Icon(Icons.Default.Analytics, contentDescription = null, tint = Color(0xFF1D4ED8), modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Infrastructure Status (97.5%)", color = Color(0xFF1D4ED8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        item {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
            modifier = Modifier.clickable { onSeeAllComplaints() }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Icon(Icons.Default.Description, contentDescription = null, tint = TextDark, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("My Reports", color = TextDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
          }
        }

        item {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder),
            modifier = Modifier.clickable { onOpenEmergencyContacts() }
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Icon(Icons.Default.Phone, contentDescription = null, tint = TextDark, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Emergency Contacts", color = TextDark, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
          }
        }
      }
    }

    // National Infrastructure Condition Matrix Teaser Card matching Screenshot 1
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp)
          .clickable { onOpenInfrastructureStatus() }
          .testTag("home_infrastructure_matrix_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF93C5FD))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(20.dp)
              )
              Text(
                text = "National Infrastructure Matrix",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFEFF6FF)
            ) {
              Text(
                text = "Live Matrix →",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2563EB),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4 Grid indicator bars (Electricity, Water, Roads, Waste)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Electricity
            Column(modifier = Modifier.weight(1f)) {
              Text("Electricity", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
              Text("97.5%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
              LinearProgressIndicator(
                progress = { 0.975f },
                color = Color(0xFF2563EB),
                trackColor = Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
              )
            }

            // Water
            Column(modifier = Modifier.weight(1f)) {
              Text("Water", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
              Text("89.1%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
              LinearProgressIndicator(
                progress = { 0.891f },
                color = Color(0xFF0284C7),
                trackColor = Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
              )
            }

            // Roads
            Column(modifier = Modifier.weight(1f)) {
              Text("Roads", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
              Text("94.2%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
              LinearProgressIndicator(
                progress = { 0.942f },
                color = Color(0xFF475569),
                trackColor = Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
              )
            }

            // Waste
            Column(modifier = Modifier.weight(1f)) {
              Text("Waste", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
              Text("96.4%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
              LinearProgressIndicator(
                progress = { 0.964f },
                color = ForestGreen,
                trackColor = Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp))
              )
            }
          }
        }
      }
    }

    // Categories Section
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = 20.dp)
      ) {
        Text(
          text = "Categories",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 4x2 grid of category cards
        for (rowIndex in 0..1) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            for (colIndex in 0..3) {
              val itemIndex = rowIndex * 4 + colIndex
              if (itemIndex < categories.size) {
                val item = categories[itemIndex]
                Card(
                  modifier = Modifier
                    .weight(1f)
                    .clickable {
                      if (item.isMore) {
                        onMoreCategories()
                      } else {
                        item.category?.let { onCategorySelected(it) }
                      }
                    }
                    .testTag("category_card_${item.label.lowercase()}"),
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(vertical = 14.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Box(
                      modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(item.iconColor.copy(alpha = 0.12f)),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = item.iconColor,
                        modifier = Modifier.size(22.dp)
                      )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                      text = item.label,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium,
                      color = TextDark,
                      maxLines = 1
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Recent Reports Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Recent Reports",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextDark
        )

        Text(
          text = "See all →",
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
          color = ForestGreen,
          modifier = Modifier
            .clickable { onSeeAllComplaints() }
            .testTag("see_all_reports_link")
        )
      }
    }

    // Recent Reports List
    if (recentComplaints.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Outlined.Inbox,
              contentDescription = null,
              tint = TextMuted,
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "No reports submitted yet",
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextDark
            )
            Text(
              text = "Tap 'Report an Issue' to file a community problem.",
              fontSize = 13.sp,
              color = TextMuted
            )
          }
        }
      }
    } else {
      items(recentComplaints.take(2)) { complaint ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onComplaintClick(complaint.id) }
            .testTag("recent_complaint_${complaint.id}"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(UrgencyHighBg.copy(alpha = 0.5f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.DeleteOutline,
                  contentDescription = null,
                  tint = UrgencyHigh,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = complaint.category.shortName,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = UrgencyHigh
                )
              }

              StatusBadge(status = complaint.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = complaint.title,
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
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${complaint.locationName}, ${complaint.assignedInstitution ?: "Municipal Council"}",
                fontSize = 13.sp,
                color = TextMuted,
                maxLines = 1
              )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = complaint.id,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
              )
              Text(
                text = complaint.createdAt.substringBefore(" ·"),
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }
        }
      }
    }

    // Report Update Banner
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 6.dp)
          .clickable { onAlertBannerClick("CG-2026-01482") }
          .testTag("report_update_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(ForestGreen),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Report Update",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = TextDark
            )
            Text(
              text = "KMC is checking your reported location.",
              fontSize = 12.sp,
              color = TextMuted
            )
          }
        }
      }
    }
  }
}
