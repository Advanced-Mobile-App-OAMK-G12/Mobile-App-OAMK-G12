package com.example.advancedandroidcourse.presentation.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.presentation.main.PostViewModel
import com.example.advancedandroidcourse.ui.theme.BackgroundColor
import com.example.advancedandroidcourse.ui.theme.MainTextColor
import java.nio.file.WatchEvent

@Composable
fun PostItem(
    postDetails: PostDetails,
    showAuthorInfo: Boolean,
    onToggleFavorited: () -> Unit,
    navController: NavController,
) {

    Column (
        modifier = Modifier
            .padding(4.dp)
            .shadow(
                4.dp,
                shape = RoundedCornerShape(4.dp),
                ambientColor = MainTextColor.copy(alpha = 0.2f),
                spotColor = MainTextColor.copy(alpha = 0.4f)
            )
            .clip(RoundedCornerShape(4.dp))
            .background(BackgroundColor)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
//                        onPress = {},
                        onTap = {
                            Log.d(
                                "HomeScreen",
                                "Navigating to postDetails with tipId: ${postDetails.post.id}"
                            )
                            navController.navigate("postDetails/${postDetails.post.id}")
                        }
                    )
                }
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = rememberImagePainter(postDetails.post.images[0]),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = postDetails.post.title,
                modifier = Modifier.padding(8.dp)
            )
        }

//  Display author's information
        if (showAuthorInfo) {
            AuthorInfo(
                userAvatar = postDetails.user.image,
                userName = postDetails.user.name,
                isFavorited = postDetails.post.savedCount > 0,
                onToggleFavorited = onToggleFavorited,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}