package com.example.advancedandroidcourse.presentation.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.presentation.auth.AuthState
import com.example.advancedandroidcourse.presentation.auth.AuthViewModel
import com.example.advancedandroidcourse.presentation.composables.BottomBar
import com.example.advancedandroidcourse.presentation.composables.PostItem
import com.example.advancedandroidcourse.presentation.composables.SearchBar

@Composable
fun HomeScreen(
    modifier : Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    postViewModel: PostViewModel = hiltViewModel()
){
    val authState = authViewModel.authState.observeAsState()
    val posts by postViewModel.posts.collectAsState(emptyList())

    Log.d("HomeScreen", "Fetched posts: $posts")

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    var searchValue by remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        postViewModel.getPosts()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 6.dp, top = 42.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "easy Finn Logo",
                    modifier = Modifier.size(80.dp)
                )
                SearchBar(
                    value = searchValue,
                    onValueChange = { searchValue = it },
                    iconRes = R.drawable.search,
                    onSearchClick = {
                        navController.navigate("search")
                    }
                )
            }

            //        List Posts
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(posts) { postDetails ->
                    PostItem(postDetails)
                }
            }

            //        Sign Out text
            TextButton(onClick = {
                authViewModel.signout()
            }) {
                Text(text = "Sign out")
            }
        }
        BottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}