package com.example.advancedandroidcourse.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.advancedandroidcourse.presentation.savedTips.SaveTipsScreen
import com.example.advancedandroidcourse.presentation.auth.AuthViewModel
import com.example.advancedandroidcourse.presentation.main.HomeScreen
import com.example.advancedandroidcourse.presentation.main.PostScreen
import com.example.advancedandroidcourse.presentation.auth.LoginScreen
import com.example.advancedandroidcourse.presentation.auth.RegisterScreen
import com.example.advancedandroidcourse.presentation.main.PostViewModel
import com.example.advancedandroidcourse.presentation.notifications.NotificationScreen
import com.example.advancedandroidcourse.presentation.notifications.NotificationViewModel
import com.example.advancedandroidcourse.presentation.postDetails.PostDetailsScreen
import com.example.advancedandroidcourse.presentation.profile.EditProfileScreen
import com.example.advancedandroidcourse.presentation.profile.ProfileScreen
import com.example.advancedandroidcourse.presentation.search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController,modifier: Modifier = Modifier,isUserLoggedIn: Boolean) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val startDestination = if (isUserLoggedIn) "home" else "login"
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(modifier, navController, authViewModel)
        }
        composable("signup") {
            RegisterScreen(modifier, navController, authViewModel)
        }
        composable("home") {
            HomeScreen(modifier, navController)
        }
        composable(
            route = "postDetails/{tipId}",
            arguments = listOf(navArgument("tipId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tipId = backStackEntry.arguments?.getString("tipId")
            if (tipId != null) {
                PostDetailsScreen(tipId = tipId, navController = navController)
            }
        }
        composable("postScreen") {
            val postViewModel: PostViewModel = hiltViewModel()
            PostScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(navController,authViewModel = authViewModel)
        }
        composable("editProfile") {
            EditProfileScreen(navController)
        }
        composable("search") {
            SearchScreen(navController)
        }
        composable("notifications") {
            val viewModel: NotificationViewModel = hiltViewModel()
            NotificationScreen(
                onTipClick = { tipId -> navController.navigate("postDetails/$tipId") },
                viewModel = viewModel,
                navController = navController
            )
            LaunchedEffect(Unit) {
                viewModel.markNotificationsAsRead()
            }
        }
        composable("savedTips") {
            SaveTipsScreen(navController)
        }
    }
}
//Navigation with Jetpack Navigation Component