package com.example.advancedandroidcourse.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.advancedandroidcourse.presentation.composables.SearchBar
import com.example.advancedandroidcourse.presentation.composables.SectionTitle
import com.example.advancedandroidcourse.presentation.composables.TipItem
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.model.User
import com.example.advancedandroidcourse.presentation.composables.PostItem

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
    val hotTips by viewModel.hotTips.collectAsState()
    val latestTips by viewModel.latestTips.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() //Add loading state
    val errorMessage by viewModel.errorMessage.collectAsState() // Add error message state
    val searchPerformed by viewModel.searchPerformed.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshTips()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        //Back arrow and Search bar
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }
        SearchBar(
            value = searchText, onValueChange = {
                searchText = it
                viewModel.clearSearchResults() //Clear old results when new one typing
            },
            onSearchClick = {
                viewModel.searchTips(searchText)

            },
            iconRes = android.R.drawable.ic_menu_search
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
        }


            LazyColumn(modifier = Modifier.fillMaxSize()) {
             if (searchText.isNotEmpty()) {
            items(searchResults) { tip ->
              TipItem(tip = tip, navController = navController, viewModel = viewModel)//Click navigate to PostDetails Screen
             }

              if (searchPerformed && searchResults.isEmpty()) {
             item {
                Text("No results found", modifier = Modifier.padding(16.dp))
              }
             }
        } else {
            //Hot Tips section
                item {
                    Text(
                        text = "Hot",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                if (hotTips.isNotEmpty()) {
                    items(hotTips) { tip ->
                        TipItem(tip = tip, navController = navController, viewModel = viewModel)
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    item {
                        Text("no hot tips available", modifier = Modifier.padding(16.dp))
                    }
                }

                //Latest Tips section
                item {
                    Text(
                        text = "Latest",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                if (latestTips.isNotEmpty()) {
                    items(latestTips) { tip ->
                        TipItem(tip = tip, navController = navController, viewModel = viewModel)
                    }
                } else {
                    item {
                        Text("No latest tips available", modifier = Modifier.padding(16.dp))
                    }
                }

            }
        }
    }
}