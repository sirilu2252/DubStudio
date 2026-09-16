package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DubStudioColorScheme = darkColorScheme(
    primary = AccentPurple,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceElevated,
    onPrimaryContainer = AccentPurpleGlow,
    secondary = AccentCyan,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurfaceHighlight,
    onSecondaryContainer = AccentCyanGlow,
    tertiary = AccentEmerald,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent dark studio aesthetic
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DubStudioColorScheme,
        typography = Typography,
        content = content
    )
}
