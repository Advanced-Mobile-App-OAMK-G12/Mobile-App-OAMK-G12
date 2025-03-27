package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.advancedandroidcourse.data.model.Post

@Composable
fun PostItem(post: Post) {
    Column (
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = post.title)
        Text(text = post.content)
    }
}