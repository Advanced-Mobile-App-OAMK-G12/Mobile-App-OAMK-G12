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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.advancedandroidcourse.R
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
            .height(56.dp)
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach{ (icon, description) ->

            val isSelected = currentRoute == description
            val scale = remember { Animatable(1f) }

            IconButton(
                onClick = {
                    when (description) {
                        add -> navController.navigate("postScreen")
                        profile -> navController.navigate("profile")
                        home -> navController.navigate("home")
                        notification -> navController.navigate("notifications")
                        saved -> navController.navigate("savedTips")
                    }
                },
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                scale.animateTo(1.2f)
                                try {
                                    this.awaitRelease()
                                } catch (
                                    _: Exception
                                ) {}
                                scale.animateTo(1f)
                Box(
                    modifier = Modifier
                        .then(
                            if (isSelected) {
                                Modifier.border(2.dp, MaterialTheme.colorScheme.mainTextColor)
                            } else {
                                Modifier
                            }
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = {})
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            when (description) {
                                add -> navController.navigate("postScreen")
                                profile -> navController.navigate("profile")
                                home -> navController.navigate("home")
                                notification -> navController.navigate("notifications")
                                saved -> navController.navigate("favoriteTips")
                            }
                        }
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = description,
                        modifier = Modifier
                            .size(if (isSelected) 32.dp else 28.dp)
                        )

                    if (description == notification && hasUnreadNotifications) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(MaterialTheme.colorScheme.error, CircleShape)
                        )
                    }
            }
        }
    }
}