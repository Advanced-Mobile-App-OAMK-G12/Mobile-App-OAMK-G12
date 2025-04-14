package com.example.advancedandroidcourse.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.ui.theme.BackgroundColor
import com.example.advancedandroidcourse.ui.theme.mainTextColor

@Composable
fun BottomBar (
    navController: NavHostController,
    hasUnreadNotifications: Boolean,
    modifier: Modifier = Modifier
) {
    val home = stringResource(R.string.home)
    val saved = stringResource(R.string.saved)
    val add = stringResource(R.string.add)
    val notification = stringResource(R.string.notification)
    val profile = stringResource(R.string.profile)

    val items = listOf(
        R.drawable.home_outline to home,
        R.drawable.save to saved,
        R.drawable.add to add,
        R.drawable.notification to notification,
        R.drawable.account to profile
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(BackgroundColor),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (defaultIcon, description) ->

            val isSelected = when (description) {
                home -> currentRoute == "home"
                saved -> currentRoute == "savedTips"
                add -> currentRoute == "postScreen"
                notification -> currentRoute == "notifications"
                profile -> currentRoute?.startsWith("profile") == true
                else -> false
            }

            val iconRes = when {
                description == notification -> {
                    if (hasUnreadNotifications) {
                        if (isSelected) R.drawable.notification_wght
                        else R.drawable.notifications_unread
                    } else {
                        if (isSelected) R.drawable.notification_wght
                        else R.drawable.notification
                }
            }
                isSelected -> when (description) {
                    home -> R.drawable.home_wght
                    saved -> R.drawable.save_wght
                    profile -> R.drawable.account_wght
                    add -> R.drawable.add_wght
                    else -> defaultIcon
                }
                else -> defaultIcon
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                    when (description) {
                        add -> navController.navigate("postScreen")
                        profile -> navController.navigate("profile")
                        home -> navController.navigate("home")
                        notification -> navController.navigate("notifications")
                        saved -> navController.navigate("savedTips")
                    }
                }
            ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = description,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
