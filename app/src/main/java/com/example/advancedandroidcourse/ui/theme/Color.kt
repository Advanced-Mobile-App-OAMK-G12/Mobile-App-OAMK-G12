package com.example.advancedandroidcourse.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LogoColor = Color(0xFF0F3F6B)
val MainTextColor = Color(0xFF112A46)
val BackgroundColor = Color(0xFFF8F9FA)
val HighlightColor  = Color(0xFFFF0505)

val ColorScheme.mainTextColor: Color
    @Composable
    get() = secondary

val ColorScheme.BackgroundColor: Color
    @Composable
    get() = BackgroundColor

val ColorScheme.HighlightColor: Color
    @Composable
    get() = HighlightColor