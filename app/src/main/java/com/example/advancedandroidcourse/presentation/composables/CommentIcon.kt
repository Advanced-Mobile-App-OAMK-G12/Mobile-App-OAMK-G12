package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.ui.theme.LogoColor
import com.example.advancedandroidcourse.ui.theme.MainTextColor

@Composable
fun CommentIcon(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.comment),
                contentDescription = "Comment",
                modifier = Modifier.size(32.dp),
                tint = MainTextColor
            )
        }
    }
}