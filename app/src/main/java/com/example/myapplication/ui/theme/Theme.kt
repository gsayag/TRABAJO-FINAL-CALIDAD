package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = FugaGreen,
    secondary = FugaGreenDark,
    background = FugaBg,
    surface = FugaPanel,
    surfaceVariant = FugaPanel2,
    onPrimary = FugaBg,
    onBackground = FugaText,
    onSurface = FugaText,
    onSurfaceVariant = FugaMuted,
    outline = FugaBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
