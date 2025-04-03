package com.example.advancedandroidcourse.presentation.postDetails

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.data.model.Comment
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.presentation.main.PostViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailsScreen(
    tipId: String,
    navController: NavController
) {

    val viewModel: PostDetailsViewModel = hiltViewModel()
    val postDetails = viewModel.postDetails.value

    LaunchedEffect(tipId) {
        viewModel.getPostDetails(tipId)
    }

    if (postDetails != null) {
        val images = postDetails.post.images
        val pagerState = rememberPagerState { images.size }

        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ){
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Back To HomeScreen",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(postDetails.user.image),
                        contentDescription = "Author Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = postDetails.user.name,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                IconButton(onClick = {
                    val shareableLink = "http://easyfinn.com/${postDetails.post.id}"
                    Log.d("Share", "Share this link: $shareableLink")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share Post",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                Image(
                    painter = rememberImagePainter(images[page]),
                    contentDescription = "Post Image",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = postDetails.post.title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = postDetails.post.content)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${postDetails.post.timestamp}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}