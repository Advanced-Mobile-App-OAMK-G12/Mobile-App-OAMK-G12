package com.example.advancedandroidcourse.presentation.postDetails

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.presentation.comment.CommentItem
import com.example.advancedandroidcourse.presentation.comment.PostCommentInput
import com.example.advancedandroidcourse.presentation.composables.CommentIcon
import com.example.advancedandroidcourse.presentation.composables.FavoriteIcon
import com.example.advancedandroidcourse.presentation.composables.SaveIcon
import com.example.advancedandroidcourse.presentation.composables.formatToDate
import com.example.advancedandroidcourse.presentation.main.PostViewModel
import com.example.advancedandroidcourse.ui.theme.LogoColor
import com.example.advancedandroidcourse.ui.theme.MainTextColor
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
    val PostDeatilsViewModel: PostDetailsViewModel = hiltViewModel()

    val postDetails = PostDeatilsViewModel.postDetails.value

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHost(hostState = snackbarHostState)

    val comments by PostDeatilsViewModel.comments.collectAsState()

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutienScope = rememberCoroutineScope()

    LaunchedEffect(tipId) {
        PostDeatilsViewModel.getPostDetails(tipId)
        PostDeatilsViewModel.getComments(tipId)
        PostDeatilsViewModel.checkIfSaved(tipId)
        PostDeatilsViewModel.fetchSavedCount(tipId)
        PostDeatilsViewModel.fetchCommentCount(tipId)
    }

    if (postDetails != null) {
        val images = postDetails.post.images
        val pagerState = rememberPagerState { images.size }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .zIndex(2f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back To HomeScreen",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 52.dp)
                        .zIndex(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //                    AuthorInfo
                        Image(
                            painter = rememberImagePainter(postDetails.user.image),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Author Avatar",
                            modifier = Modifier
                                .size(32.dp)
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

            Column {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 86.dp,bottom = 68.dp)
                ) {
                    //          PostImage
                    item {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        ) { page ->
                            Image(
                                painter = rememberImagePainter(images[page]),
                                contentDescription = "Post Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }

                    //          PostDetails
                    item {
                        Spacer(modifier = Modifier.height(4.dp))

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
                        //Add comment
                        PostCommentInput(
                            tipId = tipId,
                            modifier = Modifier
                                .bringIntoViewRequester(bringIntoViewRequester),
                            onCommentAdded = {
                                PostDeatilsViewModel.getComments(tipId)
                            }
                        )
                    }
                    items(comments) { commentDetails ->
                        CommentItem(commentDetails)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //FavoriteButton
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val isFavorited = postDetails.post.savedCount > 0

                    FavoriteIcon(
                        isFavorited = isFavorited,
                        onToggleFavorited = {
                            val newSavedCount =
                                if (isFavorited) postDetails.post.savedCount - 1
                                else postDetails.post.savedCount + 1

                            PostDeatilsViewModel.updateFavoriteCount(
                                postDetails.post.id,
                                newSavedCount
                            )
                        }
                    )
                    Text(text = "${postDetails?.post?.savedCount ?: 0}")
                }

                //SaveButton
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SaveIcon(
                        isSaved = PostDeatilsViewModel.isSaved.value,
                        onToggleSaved = {
                            PostDeatilsViewModel.toggleSaveTip(tipId)
                        }
                    )
                    Text(text = "${PostDeatilsViewModel.savedCount.value ?: 0}")
                }

                //CommentIcon
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CommentIcon(
                        onClick = {
                            coroutienScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    )
                    Text(text = "${PostDeatilsViewModel.commentCount.value ?: 0}")
                }
            }
        }
    }
}