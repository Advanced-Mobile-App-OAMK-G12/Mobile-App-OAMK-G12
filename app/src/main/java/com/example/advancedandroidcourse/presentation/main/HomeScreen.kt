package com.example.advancedandroidcourse.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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

    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val hasUnreadNotifications by notificationViewModel.hasUnreadNotifications.collectAsState()

    var selectedTab by remember { mutableStateOf("DISCOVER") }

    LaunchedEffect(selectedTab) {
        when(selectedTab) {
            "LATEST" -> postViewModel.loadLatestPosts()
            "HOT" -> postViewModel.loadHotPosts()
            "DISCOVER" -> postViewModel.loadRandomPosts()
        }
    }

    Scaffold(
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
//        PostsList
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(0.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts.size) { index ->
                    val postDetails = posts[index]
                    val isFavorited = postDetails.post.savedCount > 0

                    PostItem(
                        postDetails = postDetails,
                        onToggleFavorited = {
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
}