package com.example.advancedandroidcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.advancedandroidcourse.presentation.main.HomeScreen
import com.example.advancedandroidcourse.ui.theme.AdvancedAndroidCourseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdvancedAndroidCourseTheme {
                    EasyFinnApp(
                        HomeScreen()
                    )
            }
        }
    }
}

@Composable
fun EasyFinnApp(homeScreen: Unit) {

}

@Preview(showBackground = true)
@Composable
fun EasyFinnPreview() {
    AdvancedAndroidCourseTheme {
        EasyFinnApp(HomeScreen())
    }
}