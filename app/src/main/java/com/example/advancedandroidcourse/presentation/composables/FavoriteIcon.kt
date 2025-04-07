package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.advancedandroidcourse.R


@Composable
fun FavoriteIcon(
    modifier: Modifier = Modifier,
    isSaved: Boolean,
    onToggleSaved: () -> Unit
) {

    IconButton(
        onClick = onToggleSaved,
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            painter = painterResource(id = if (isSaved) R.drawable.favorite_filled else R.drawable.favorite),
            contentDescription = "Save",
            tint = Color.Red
        )
    }
}