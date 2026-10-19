package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComplaintStatus
import com.example.ui.theme.*

@Composable
fun StatusBadge(
  status: ComplaintStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, borderColor) = when (status) {
    ComplaintStatus.RECEIVED -> Triple(StatusReceivedBg, StatusReceivedText, StatusReceivedBorder)
    ComplaintStatus.ASSIGNED,
    ComplaintStatus.IN_PROGRESS -> Triple(StatusInProgressBg, StatusInProgressText, StatusInProgressBorder)
    ComplaintStatus.RESOLVED -> Triple(StatusResolvedBg, StatusResolvedText, StatusResolvedBorder)
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .border(1.dp, borderColor, RoundedCornerShape(12.dp))
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    when (status) {
      ComplaintStatus.RECEIVED -> {
        Icon(
          imageVector = Icons.Outlined.Inbox,
          contentDescription = null,
          tint = textColor,
          modifier = Modifier.size(13.dp)
        )
      }
      ComplaintStatus.ASSIGNED,
      ComplaintStatus.IN_PROGRESS -> {
        Box(
          modifier = Modifier
            .size(7.dp)
            .clip(CircleShape)
            .background(textColor)
        )
      }
      ComplaintStatus.RESOLVED -> {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = null,
          tint = textColor,
          modifier = Modifier.size(13.dp)
        )
      }
    }

    Text(
      text = status.label,
      color = textColor,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}
