package com.example.advancedandroidcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.advancedandroidcourse.navigation.NavGraph
import com.example.advancedandroidcourse.presentation.main.HomeScreen
import com.example.advancedandroidcourse.ui.theme.AdvancedAndroidCourseTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
        setContent {
            AdvancedAndroidCourseTheme {
                val navController = rememberNavController()

               Scaffold { innerPadding ->
                    NavGraph(
                        navController = navController,
                        isUserLoggedIn = isUserLoggedIn,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

