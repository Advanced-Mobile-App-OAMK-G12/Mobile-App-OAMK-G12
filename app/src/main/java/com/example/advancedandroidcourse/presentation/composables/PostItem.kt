package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun PostItem(
    postDetails: PostDetails,
    buttonContent: @Composable (Modifier) -> Unit,
    showAuthorInfo: Boolean
) {
    Column (
        modifier = Modifier.padding(8.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(postDetails.post.images[0]),
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = postDetails.post.title)
            Text(text = postDetails.post.content)
        }

//  Display author's information
        if (showAuthorInfo) {
            AuthorInfo(
                userAvatar = postDetails.userAvatar,
                userName = postDetails.userName,
                buttonContent = buttonContent
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}