package com.example.advancedandroidcourse.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.advancedandroidcourse.presentation.auth.AuthViewModel
import com.example.advancedandroidcourse.presentation.main.HomeScreen
import com.example.advancedandroidcourse.presentation.main.PostScreen
import com.example.advancedandroidcourse.presentation.auth.LoginScreen
import com.example.advancedandroidcourse.presentation.auth.RegisterScreen
import com.example.advancedandroidcourse.presentation.main.PostViewModel
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
            HomeScreen(modifier, navController, authViewModel)
        }
        composable("postScreen") {
            val postViewModel: PostViewModel = hiltViewModel()
            PostScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("editProfile") {
            EditProfileScreen(navController)
        }
        composable("search") {
            SearchScreen(navController)
        }
    }
}
//Navigation with Jetpack Navigation Component