package com.example.suryashakti2.ui.theme

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 🔶 DARK THEME (PRIMARY MODE)
private val DarkColorScheme = darkColorScheme(
    primary = SolarYellow,
    onPrimary = OnPrimaryBlack,

    secondary = StatusAmber,
    onSecondary = OnPrimaryBlack,

    background = DarkBackground,
    onBackground = HighContrastWhite,

    surface = SurfaceDark,
    onSurface = HighContrastWhite
)

// 🔆 LIGHT THEME (OPTIONAL)
private val LightColorScheme = lightColorScheme(
    primary = SolarYellow,
    onPrimary = OnPrimaryBlack,

    secondary = StatusAmber,
    onSecondary = OnPrimaryBlack,

    background = HighContrastWhite,
    onBackground = OnPrimaryBlack,

    surface = HighContrastWhite,
    onSurface = OnPrimaryBlack
)

@Composable
fun SuryaShaktiTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}