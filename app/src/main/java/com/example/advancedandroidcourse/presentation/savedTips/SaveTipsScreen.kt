package com.example.advancedandroidcourse.presentation.savedTips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.presentation.composables.BottomBar
import com.example.advancedandroidcourse.presentation.composables.TipCard
import com.example.advancedandroidcourse.presentation.notifications.NotificationViewModel

@Composable
fun SaveTipsScreen(
    navController: NavHostController,
    viewModel: SaveTipsViewModel = hiltViewModel()
) {
    val searchQuery by remember { mutableStateOf("") }

    val filteredTips = viewModel.tipList.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val hasUnreadNotifications by notificationViewModel.hasUnreadNotifications.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
    ) {

        //Use LazyVerticalGrid for a grid layout with 2 columns
        LazyVerticalGrid  (
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredTips) { post ->
                TipCard(post = post) {
                    navController.navigate("postDetails/${post.id}")
                }
            }

        }

        BottomBar(navController = navController, hasUnreadNotifications)
    }
}