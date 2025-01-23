package com.example.gimapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(

    titleLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),

    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 23.sp,
        textAlign = TextAlign.Center
    ),

    titleMedium = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),

    bodyMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    ),

    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),

    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    )
)