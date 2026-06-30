package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD946EF),       // Fuchsia
    secondary = Color(0xFF25F4EE),     // Cyan
    tertiary = Color(0xFF7E22CE),      // Purple
    background = Color(0xFF0F0F0F),    // Deep Black/Dark Gray
    surface = Color(0xFF171717),       // Neutral Dark Card
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFFF5F5FA),
    onSurface = Color(0xFFF5F5FA)
)

private val LightColorScheme = DarkColorScheme // Forced immersive dark theme for premium aesthetic

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force Dark for immersive theme
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve branded fuchsia styling
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
