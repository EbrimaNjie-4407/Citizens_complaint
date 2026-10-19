package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DividerColor
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.TextMuted

enum class NavDestination(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
  MY_REPORTS("My Reports", Icons.Filled.Description, Icons.Outlined.Description, "nav_my_reports"),
  ALERTS("Alerts", Icons.Filled.Notifications, Icons.Outlined.Notifications, "nav_alerts"),
  PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
}

@Composable
fun CitizenBottomNavBar(
  currentDestination: NavDestination,
  onNavigate: (NavDestination) -> Unit,
  unreadAlertsCount: Int = 0,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 8.dp
  ) {
    Column {
      HorizontalDivider(color = DividerColor, thickness = 1.dp)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(64.dp)
          .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        NavDestination.values().forEach { destination ->
          val isSelected = destination == currentDestination
          val contentColor = if (isSelected) ForestGreen else TextMuted

          Box(
            modifier = Modifier
              .weight(1f)
              .fillMaxHeight()
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 28.dp),
                onClick = { onNavigate(destination) }
              )
              .testTag(destination.testTag),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Box {
                Icon(
                  imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                  contentDescription = destination.label,
                  tint = contentColor,
                  modifier = Modifier.size(24.dp)
                )

                if (destination == NavDestination.ALERTS && unreadAlertsCount > 0) {
                  Box(
                    modifier = Modifier
                      .offset(x = 12.dp, y = (-4).dp)
                      .size(16.dp)
                      .clip(CircleShape)
                      .background(ForestGreen),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = if (unreadAlertsCount > 9) "9+" else unreadAlertsCount.toString(),
                      color = Color.White,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(3.dp))

              Text(
                text = destination.label,
                color = contentColor,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
              )
            }
          }
        }
      }
    }
  }
}
