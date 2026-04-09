package com.averyvi.bilbo.ui.theme

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.averyvi.asampleofuis.ui.theme.Typography
import com.averyvi.bilbo.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun fadedHilightText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        letterSpacing = 0.5.sp
    )
}
@Composable
fun BILBOText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily(
            Font(R.font.calistogaregular)
        ),
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.sp
    )
}
@Composable
fun topbarInstrumentText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.5.sp,
    )
}
@Composable
fun hilightText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )
}
@Composable
fun boldText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )
}
@Composable
fun semiBoldText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )
}
@Composable
fun normalText(): TextStyle {
    return TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )
}

@Composable
fun BilboTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
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
