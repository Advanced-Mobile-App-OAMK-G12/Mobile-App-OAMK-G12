package com.example.advancedandroidcourse.presentation.composables

import android.widget.NumberPicker.OnValueChangeListener
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SearchBar(
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = icon, contentDescription = "Search Icon")
            }
        }
    )
}