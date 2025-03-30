package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Black,
        modifier = Modifier.padding((8.dp))
    )
}

@Composable
fun TipItem(tip: String, navController: NavController) {
    Text(
        text = tip,
        color = Color.Black,
        modifier = Modifier.padding(8.dp)
    )
}