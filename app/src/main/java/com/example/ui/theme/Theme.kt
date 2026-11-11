package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = Color(0xFF00363D),
    primaryContainer = CyanPrimaryContainer,
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = TealSecondary,
    onSecondary = Color(0xFF00382E),
    secondaryContainer = TealSecondaryContainer,
    onSecondaryContainer = Color(0xFFA7F5E5),
    tertiary = AmberTertiary,
    onTertiary = Color(0xFF432C00),
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = Color(0xFFFFDF9E),
    error = RedAlert,
    onError = Color.White,
    background = DarkBg,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF2C3E55),
    outlineVariant = Color(0xFF1B273A)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006877),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA1EFFF),
    onPrimaryContainer = Color(0xFF001F25),
    secondary = Color(0xFF006B5D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF73F8DF),
    onSecondaryContainer = Color(0xFF00201B),
    tertiary = Color(0xFF825500),
    onTertiary = Color.White,
    background = Color(0xFFF7F9FC),
    onBackground = Color(0xFF141C24),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF141C24),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF424E5E),
    error = Color(0xFFBA1A1A),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun NeuroPsychTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Professional clinical high-clarity theme defaults to dark mode for neuroimaging & contrast
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
