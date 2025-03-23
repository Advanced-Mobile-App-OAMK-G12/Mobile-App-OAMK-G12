package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.advancedandroidcourse.R

@Composable
fun BottomBar (navController: NavHostController) {
    val home = stringResource(R.string.home)
    val hot = stringResource(R.string.hot)
    val add = stringResource(R.string.add)
    val latest = stringResource(R.string.latest)
    val profile = stringResource(R.string.profile)

    val items = listOf(
        R.drawable.home_outline to home,
        R.drawable.hot to hot,
        R.drawable.add to add,
        R.drawable.new_releases to latest,
        R.drawable.account to profile
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach{ (icon, description) ->
            IconButton(
                onClick = {
                    if (description == add) {
                        navController.navigate("postScreen")
                    }
                }
            ) {
                Icon(painter = painterResource(id = icon), contentDescription = description)
            }
        }
    }
}