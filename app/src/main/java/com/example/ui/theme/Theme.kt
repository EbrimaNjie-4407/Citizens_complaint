package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
  primary = ForestGreen,
  onPrimary = Color.White,
  primaryContainer = ForestGreenLight,
  onPrimaryContainer = ForestGreenDark,
  secondary = ForestGreenDark,
  onSecondary = Color.White,
  background = BackgroundLight,
  onBackground = TextDark,
  surface = SurfaceWhite,
  onSurface = TextDark,
  surfaceVariant = BackgroundLight,
  onSurfaceVariant = TextMuted,
  outline = OutlineBorder,
  error = UrgencyCritical,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      window?.let {
        it.statusBarColor = ForestGreen.toArgb()
        val controller = WindowCompat.getInsetsController(it, view)
        controller.isAppearanceLightStatusBars = false
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
