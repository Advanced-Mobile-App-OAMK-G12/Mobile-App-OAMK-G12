package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.rememberTopAppBarState
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
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top =36.dp)
            .height(42.dp),
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
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("HOT", "DISCOVER", "LATEST").forEach { tab ->
                    Box(
                        modifier = Modifier
                            .clickable{
                                onTabSelected(tab)
                            }
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                    ) {
                        Text(
                            text = tab,
                            fontSize = if (selectedTab == tab) 16.sp else 14.sp,
                            fontWeight = if (selectedTab == tab) FontWeight.Bold
                                else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.mainTextColor
                        )
                    }
                }
            }

//        SearchButton
            Box(
                modifier = modifier
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        navController.navigate("search")
                    },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

}