package com.example.advancedandroidcourse.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import android.util.Log
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.presentation.composables.BottomBar
import com.example.advancedandroidcourse.presentation.composables.PostItem
import com.example.advancedandroidcourse.presentation.composables.TopBar
import com.example.advancedandroidcourse.presentation.notifications.NotificationViewModel

@Composable
fun HomeScreen(
    modifier : Modifier = Modifier,
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
){

//    Collect posts from ViewModel
    val posts by postViewModel.posts.collectAsState(emptyList())
    Log.d("HomeScreen", "Posts size: ${posts.size}")
    val listState = rememberLazyGridState()
    var isLoading by remember { mutableStateOf(false) }

    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val hasUnreadNotifications by notificationViewModel.hasUnreadNotifications.collectAsState()

    var selectedTab by remember { mutableStateOf("DISCOVER") }
    Log.d("HomeScreen", "Composing HomeScreen, selectedTab = $selectedTab")

    LaunchedEffect(selectedTab) {
        when(selectedTab) {
            "LATEST" -> postViewModel.loadLatestPosts()
            "HOT" -> postViewModel.loadHotPosts()
            "DISCOVER" -> postViewModel.loadRandomPosts()
        }
    }

    LaunchedEffect(listState, selectedTab) {
        Log.d("HomeScreen", "LaunchedEffect for tab: $selectedTab")
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
        }.collect { isAtBottom ->
            if (isAtBottom && !isLoading) {
                when (selectedTab) {
                    "LATEST" -> postViewModel.loadLatestPosts()
                    "HOT" -> postViewModel.loadHotPosts()
                    "DISCOVER" -> postViewModel.loadRandomPosts()
                }
                isLoading = false
            }
        }
    }

    val postsToDisplay = when (selectedTab) {
        "LATEST" -> postViewModel.latestPosts
        "HOT" -> postViewModel.hotPosts
        "DISCOVER" -> postViewModel.randomPosts
        else -> emptyList<PostDetails>()
    }
    Log.d("HomeScreen", "postsToDisplay.size = ${postsToDisplay.size}")

    if (postsToDisplay.isEmpty()) {
        Log.d("HomeScreen", "Posts still loading or empty")
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),

        //        TopBar
        topBar = {
            TopBar(
                navController = navController,
                selectedTab = selectedTab,
                onTabSelected = { tab -> selectedTab = tab }
            )
        },
        //        BottomBar
        bottomBar = {
            BottomBar(
                navController = navController,
                modifier = Modifier.fillMaxWidth(),
                hasUnreadNotifications = hasUnreadNotifications,
            )
        },
    ) { innerPadding ->
        Log.d("HomeScreen", "postsToDisplay.size = ${postsToDisplay.size}")
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                contentPadding = PaddingValues(0.dp),
                state = listState
          ) {
                items(postsToDisplay.size) { index ->
                    Log.d("HomeScreen", "Rendering post at index $index")
                    val postDetails = postsToDisplay[index]

                    PostItem(
                        postDetails = postDetails,
                        onToggleFavorited = {
                            val isFavorited = postDetails.post.savedCount > 0
                            val newSavedCount = if (isFavorited) postDetails.post.savedCount - 1
                                else postDetails.post.savedCount + 1

                            postViewModel.updateSavedCount(postDetails.post.id, newSavedCount)
                        },
                        showAuthorInfo = true,
                        navController = navController
                    )
                }
            }
    }
}