package com.example.advancedandroidcourse.navigation

import androidx.compose.runtime.Composable
import com.example.advancedandroidcourse.presentation.PostScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

//Navigation with Jetpack Navigation Component


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "post_screen") {
        composable("post_screen") {
            PostScreen(
                onBackClick = { navController.popBackStack()},
                onPostClick = { /* post to Firebase Logic */ }
            )
        }
    }
}