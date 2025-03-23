package com.example.advancedandroidcourse.navigation

import androidx.compose.runtime.Composable
import com.example.advancedandroidcourse.presentation.main.PostScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.presentation.main.HomeScreen

//Navigation with Jetpack Navigation Component


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen(navController = navController)
        }

        composable("postScreen") {
            PostScreen(
                onBackClick = { navController.popBackStack()},
                onPostClick = { /* post to Firebase Logic */ }
            )
        }
    }
}