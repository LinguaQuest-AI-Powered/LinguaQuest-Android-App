package com.iti.linguaquest.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val BaseTextStyle = TextStyle(
    fontFamily = Quicksand
)

val AppTypography = Typography(

    displayLarge = BaseTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),

    displayMedium = BaseTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp
    ),

    displaySmall = BaseTextStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),

    headlineLarge = BaseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp
    ),

    headlineMedium = BaseTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),

    headlineSmall = BaseTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),

    titleLarge = BaseTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),

    titleMedium = BaseTextStyle.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),

    titleSmall = BaseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    bodyLarge = BaseTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodyMedium = BaseTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    bodySmall = BaseTextStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    labelLarge = BaseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),

    labelMedium = BaseTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),

    labelSmall = BaseTextStyle.copy(
        fontWeight = FontWeight.Light,
        fontSize = 11.sp
    )
)