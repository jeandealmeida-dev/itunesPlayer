package com.jeandealmeida_dev.itunesplayer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jeandealmeida_dev.itunesplayer.R

val ArticulatCF = FontFamily(
    Font(R.font.articulat_cf_medium, FontWeight.Normal),
    Font(R.font.articulat_cf_medium, FontWeight.Medium),
    Font(R.font.articulat_cf_bold, FontWeight.Bold),
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Bold, fontSize = 57.sp),
    displayMedium = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Bold, fontSize = 45.sp),
    displaySmall = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Bold, fontSize = 36.sp),
    headlineLarge = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Bold, fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 28.sp),
    headlineSmall = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Bold, fontSize = 17.sp),
    titleMedium = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 16.sp),
    titleSmall = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 15.sp),
    bodyMedium = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    labelSmall = TextStyle(fontFamily = ArticulatCF, fontWeight = FontWeight.Medium, fontSize = 11.sp),
)
