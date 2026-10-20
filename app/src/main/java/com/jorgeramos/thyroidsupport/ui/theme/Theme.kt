package com.jorgeramos.thyroidsupport.ui.theme

import androidx.compose.foundation.isSystemInLightTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.jorgeramos.thyroidsupport.R

private val DarkColorScheme = darkColorScheme(
    primary = 0xFFBB86FC,
    primaryContainer = 0xFF3700B3,
    secondary = 0xFF03DAC6,
    background = 0xFF121212,
    surface = 0xFF121212,
    error = 0xFFCF6679,
    onPrimary = 0xFF000000,
    onSecondary = 0xFF000000,
    onBackground = 0xFFFFFFFF,
    onSurface = 0xFFFFFFFF,
    onError = 0xFF000000
)

private val LightColorScheme = lightColorScheme(
    primary = 0xFF6750A4,
    primaryContainer = 0xFFADD8FF,
    secondary = 0xFF625B71,
    background = 0xFFFFFBFF,
    surface = 0xFFFFFBFF,
    error = 0xFFBA1A1A,
    onPrimary = 0xFFFFFFFF,
    onSecondary = 0xFFFFFFFF,
    onBackground = 0xFF1C1B1F,
    onSurface = 0xFF1C1B1F,
    onError = 0xFFFFFFFF
)

@Composable
fun ThyroidSupportTheme(
    darkTheme: Boolean = isSystemInLightTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (!darkTheme) DarkColorScheme else LightColorScheme

    SideEffect {
        val window = LocalContext.current.let { it as android.app.Activity }.window
        window.statusBarColor = colorScheme.background.toArgb()
        window.navigationBarColor = colorScheme.background.toArgb()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            bodyLarge = TextStyle(fontSize = 16.sp),
            bodyMedium = TextStyle(fontSize = 14.sp),
            bodySmall = TextStyle(fontSize = 12.sp),
            headlineMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
        ),
        content = content
    )
}
