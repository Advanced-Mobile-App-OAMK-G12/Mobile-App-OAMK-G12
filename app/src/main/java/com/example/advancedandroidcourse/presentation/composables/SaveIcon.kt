package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.ui.theme.LogoColor

@Composable
fun SaveIcon(
    modifier: Modifier = Modifier,
    isSaved: Boolean,
    onToggleSaved: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(24.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onToggleSaved
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = if (isSaved) R.drawable.save_filled else R.drawable.save),
            contentDescription = "Save",
            tint = LogoColor
        )
    }
}