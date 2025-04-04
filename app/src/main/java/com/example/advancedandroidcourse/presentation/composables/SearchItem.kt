package com.example.advancedandroidcourse.presentation.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.advancedandroidcourse.data.model.Tip

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Black,
        modifier = Modifier.padding((8.dp))
    )
}

@Composable
fun TipItem(tip: Tip, navController: NavController) {
    Text(
        text = tip.title,
        color = Color.Black,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                Log.d("TipItem", "Clicked on tip: ${tip.id}")
                navController.navigate("postDetails/${tip.id}")//Navigate to PostDetails Screen
            }
    )
}