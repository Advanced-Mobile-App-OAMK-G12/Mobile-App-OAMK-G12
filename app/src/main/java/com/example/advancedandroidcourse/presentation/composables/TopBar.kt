package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.ui.theme.mainTextColor

@Composable
fun TopBar(
    navController: NavHostController,
//    selectedTab: String,
//    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
){

    var selectedTab by remember { mutableStateOf("DISCOVER") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
//        Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "easy Finn Logo",
            modifier = Modifier.size(52.dp)
        )

//        Navbutton in the middle
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = {selectedTab = "HOT"},
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.mainTextColor
                )
            ) {
                Text(
                    "HOT",
                    fontSize = if (selectedTab == "HOT") 16.sp else 14.sp,
                    fontWeight = if (selectedTab == "HOT") FontWeight.Bold else FontWeight.Normal
                )
            }

            TextButton(
                onClick = { selectedTab == "DISCOVER" },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.mainTextColor
                )
            ) {
                Text(
                    "DISCOVER",
                    fontSize = if (selectedTab == "HOT") 16.sp else 14.sp,
                    fontWeight = if (selectedTab == "HOT") FontWeight.Bold else FontWeight.Normal
                )
            }

            TextButton(
                onClick = { selectedTab == "DISCOVER" },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.mainTextColor
                )
            ) {
                Text(
                    "LATEST",
                    fontSize = if (selectedTab == "HOT") 16.sp else 14.sp,
                    fontWeight = if (selectedTab == "HOT") FontWeight.Bold else FontWeight.Normal
                )
            }
        }

//        SearchButton
        IconButton(onClick = {
            navController.navigate("search")
        }) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}