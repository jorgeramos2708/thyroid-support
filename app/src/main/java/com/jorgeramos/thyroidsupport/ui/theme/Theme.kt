package com.jorgeramos.thyroidsupport.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.FontFamily
import androidx.compose.ui.text.FontStyle
import androidx.compose.ui.text.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2F6B4F),
    primaryContainer = Color(0xFFDDEBE2),
    secondary = Color(0xFF2F6B4F),
    background = Color(0xFF14152B),
    surface = Color(0xFF1E2040),
    error = Color(0xFFB3261E),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFECEDFB),
    onSurface = Color(0xFFECEDFB),
    onError = Color(0xFF1D1E3F)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2F6B4F),
    primaryContainer = Color(0xFFDDEBE2),
    secondary = Color(0xFF2F6B4F),
    background = Color(0xFFF1F5F0),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB3261E),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1E3A2F),
    onSurface = Color(0xFF1E3A2F),
    onError = Color(0xFF2A2110)
)

private val AppTypography = androidx.compose.material3.Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(f(R.fraunces_600))),
        fontSize = 28.sp,
        fontWeight = FontWeight(600),
        fontStyle = FontStyle.Normal
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(f(R.fraunces_600))),
        fontSize = 26.sp,
        fontWeight = FontWeight(600),
        fontStyle = FontStyle.Normal
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(f(R.roboto_500))),
        fontSize = 16.sp,
        fontWeight = FontWeight(500),
        fontStyle = FontStyle.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(f(R.roboto_500))),
        fontSize = 15.sp,
        fontWeight = FontWeight(500),
        fontStyle = FontStyle.Normal
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(f(R.roboto_400))),
        fontSize = 12.sp,
        fontWeight = FontWeight(400),
        fontStyle = FontStyle.Normal
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(f(R.roboto_700))),
        fontSize = 16.sp,
        fontWeight = FontWeight(700),
        fontStyle = FontStyle.Normal
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(f(R.roboto_500))),
        fontSize = 13.sp,
        fontWeight = FontWeight(500),
        fontStyle = FontStyle.Normal
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(f(R.roboto_600))),
        fontSize = 12.sp,
        fontWeight = FontWeight(600),
        fontStyle = FontStyle.Normal
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(f(R.fraunces_600))),
        fontSize = 28.sp,
        fontWeight = FontWeight(600),
        fontStyle = FontStyle.Normal
    )
)

@Composable
fun ThyroidSupportTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    SideEffect {
        val window = (LocalView.current.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, LocalView.current).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
