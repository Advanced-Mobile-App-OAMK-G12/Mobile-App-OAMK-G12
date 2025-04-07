package com.example.advancedandroidcourse.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.presentation.composables.BottomBar
import com.example.advancedandroidcourse.presentation.composables.PostItem
import com.example.advancedandroidcourse.presentation.composables.TopBar

@Composable
fun HomeScreen(
    modifier : Modifier = Modifier,
    navController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel()
){

//    Collect posts from ViewModel
    val posts by postViewModel.posts.collectAsState(emptyList())

//    Ensure posts init when HomeScreen is displayed
    LaunchedEffect(Unit) {
        postViewModel.getPosts()
    }

    Scaffold(
        //        TopBar
        topBar = {
            TopBar(
                navController = navController,
                modifier = Modifier.padding(top = 24.dp)
                )
        },
        //        BottomBar
        bottomBar = {
            BottomBar(navController = navController)
        }

    ) { innerPadding ->
//        PostsList
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
//                .padding(top = 4.dp),

//            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(4.dp),
                contentPadding = PaddingValues(bottom = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(posts.size) { index ->
                    val postDetails = posts[index]
                    val isSaved = postDetails.post.savedCount > 0

                    PostItem(
                        postDetails = postDetails,
                        onToggleSaved = {
                            val newSavedCount = if (isSaved) postDetails.post.savedCount - 1
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