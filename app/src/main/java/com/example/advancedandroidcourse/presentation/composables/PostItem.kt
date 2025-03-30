package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.PostDetails
import java.nio.file.WatchEvent

@Composable
fun PostItem(postDetails: PostDetails) {
    Column (
        modifier = Modifier.padding(8.dp)
    ) {
        Column {
            Text(text = postDetails.post.title)
            Text(text = postDetails.post.content)
        }
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(postDetails.userAvatar),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = postDetails.userName, fontWeight = FontWeight.Bold)

        }
        Spacer(modifier = Modifier.height(8.dp))

    }
}