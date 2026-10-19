package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenLight
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.delay

@Composable
fun VoiceNotePlayer(
  durationText: String = "0:12",
  modifier: Modifier = Modifier
) {
  var isPlaying by remember { mutableStateOf(false) }
  var secondsElapsed by remember { mutableIntStateOf(0) }
  val totalSeconds = 12

  LaunchedEffect(isPlaying) {
    if (isPlaying) {
      while (isPlaying && secondsElapsed < totalSeconds) {
        delay(1000)
        secondsElapsed += 1
      }
      if (secondsElapsed >= totalSeconds) {
        isPlaying = false
        secondsElapsed = 0
      }
    }
  }

  val displaySeconds = if (isPlaying) secondsElapsed else totalSeconds
  val formattedTime = String.format("0:%02d", displaySeconds)

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color.White)
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    // Play / Pause Circle
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(ForestGreenLight)
        .clickable {
          if (secondsElapsed >= totalSeconds) secondsElapsed = 0
          isPlaying = !isPlaying
        }
        .testTag("play_pause_voice_note"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
        contentDescription = if (isPlaying) "Pause voice note" else "Play voice note",
        tint = ForestGreen,
        modifier = Modifier.size(24.dp)
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = "Voice note",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = TextDark
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Waveform bars
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        val barHeights = listOf(
          8, 14, 20, 10, 18, 24, 16, 22, 12, 18, 20, 14, 22, 16, 10, 18, 24, 12, 16, 8, 14, 10
        )
        barHeights.forEachIndexed { index, height ->
          val progressRatio = secondsElapsed.toFloat() / totalSeconds.toFloat()
          val barRatio = index.toFloat() / barHeights.size.toFloat()
          val isPassed = isPlaying && barRatio <= progressRatio

          Box(
            modifier = Modifier
              .width(3.dp)
              .height(height.dp)
              .clip(RoundedCornerShape(1.5.dp))
              .background(if (isPassed) ForestGreen else TextMuted.copy(alpha = 0.4f))
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(10.dp))

    Text(
      text = formattedTime,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = TextMuted
    )
  }
}
