package com.example.common

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color


val colorBackgroundDark = Color(0xFF1e1f22)
val colorBackgroundDark2 = Color(0xFF3d3f41)

val colorGrayWindow = Color(0xFF2b2d30)
//val colorGrayWindow2 = Color(0xFF2a2c34) // earlier
val colorGrayWindow2 = Color(0xFF000000)

//val colorCard = Color(0xFF313b49)
val colorCard = Color(0xFF16181c)
val fontTitleMonth = Color(0xFFe1e2e4)
val colorTextSumMonth = Color(0xFFbfc9d4)
val colorText= Color(0xFFe7e9ea)
val colorTextSecondary= Color(0xFF71767a)

//private val LightColors = lightColors(
//    primary = md_theme_light_primary,
//)

object AppTheme {

//    val colors: FinColors
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalColors.current

//    val typography: AppTypography
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalTypography.current
//
//    val dimensions: AppDimensions
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalDimensions.current
}

fun darkFinColors(
    background :Color = Color(0xFF2a2c34),
    cardBackground :Color = Color(0xFF313b49),
    error :Color = Color.Red,

    titleCardMonth :Color = Color(0xFFc6cfdb),
    commonSum  :Color= Color(0xFFc6cfdb),
    isLight :Boolean = false,
): FinColors = FinColors(
    background = background,
    cardBackground,
    error,
    titleCardMonth,
    commonSum,
    isLight
)

@Stable
class FinColors(
//    primary: Color,
//    primaryVariant: Color,
//    secondary: Color,
//    secondaryVariant: Color,

    background: Color,
    cardBackground: Color,
    error: Color,

    titleCardMonth: Color,
    commonSum: Color,


//    onPrimary: Color,
//    onSecondary: Color,
//    onBackground: Color,
//    onSurface: Color,
//    onError: Color,

    isLight: Boolean
) {

}