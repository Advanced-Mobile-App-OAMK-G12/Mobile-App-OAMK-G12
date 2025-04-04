package com.example.advancedandroidcourse.presentation.postDetails

import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.presentation.comment.CommentItem
import com.example.advancedandroidcourse.presentation.comment.PostCommentInput
import com.example.advancedandroidcourse.presentation.composables.formatToDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun shareContent(tipId: String, context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = android.content.ClipData.newPlainText("Post Link", "http://easyfinn.com/tip/$tipId")
    clipboard.setPrimaryClip(clip)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDetailsScreen(
    tipId: String,
    navController: NavController,
) {

    val viewModel: PostDetailsViewModel = hiltViewModel()
    val postDetails = viewModel.postDetails.value

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackbarHostState)

    val comments by viewModel.comments.collectAsState()

    LaunchedEffect(tipId) {
        viewModel.getPostDetails(tipId)
        viewModel.getComments(tipId)
    }

    if (postDetails != null) {
        val images = postDetails.post.images
        val pagerState = rememberPagerState { images.size }

        Column (
            modifier = Modifier.padding(start = 6.dp, top = 8.dp)
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
//                    AuthorInfo
                    Image(
                        painter = rememberImagePainter(postDetails.user.image),
                        contentDescription = "Author Avatar",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = postDetails.user.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

//                ShareButton
                IconButton(onClick = {
                    shareContent(tipId, context)
                    CoroutineScope(Dispatchers.Main).launch {
                        snackbarHostState.showSnackbar("Post link copied!")
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share Post",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

//          PostImage
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

//          PostDetails
            Text(
                text = postDetails.post.title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = postDetails.post.content)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = postDetails.post.timestamp.formatToDate(),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

//            Comments
            PostCommentInput(
                tipId = tipId,
                onCommentAdded = {
                    viewModel.getComments(tipId)
                }
            )

            LazyColumn {
                items(comments) { commentDetails ->
                    CommentItem(commentDetails)
                }
            }
        }
    }
}