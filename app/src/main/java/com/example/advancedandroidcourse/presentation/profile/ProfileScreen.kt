package com.example.advancedandroidcourse.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.advancedandroidcourse.data.model.Tip

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val favorites by viewModel.favoriteTips.collectAsState()
    val posts by viewModel.postedTips.collectAsState()
    var selectedTab by remember { mutableStateOf("favorites") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Profile Image
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user?.image ?: "")
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // (Optional) Add "+" icon here for image change
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = user?.name ?: "Username", style = MaterialTheme.typography.titleMedium)
        Text(text = user?.bio ?: "No bio", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(12.dp))

        // Edit button
        IconButton(onClick = { navController.navigate("editProfile") }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Toggle Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconToggleButton(
                checked = selectedTab == "favorites",
                onCheckedChange = { selectedTab = "favorites" }
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorites")
            }

            IconToggleButton(
                checked = selectedTab == "posts",
                onCheckedChange = { selectedTab = "posts" }
            ) {
                Icon(Icons.Default.PostAdd, contentDescription = "My Posts")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tips List
        val tipsToShow = if (selectedTab == "favorites") favorites else posts

        if (tipsToShow.isEmpty()) {
            Text("No tips to show.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tipsToShow.forEach { tip ->
                    TipCard(tip)
                }
            }
        }
    }
}

@Composable
fun TipCard(tip: Tip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = tip.title, style = MaterialTheme.typography.titleMedium)
            Text(text = tip.content, style = MaterialTheme.typography.bodySmall)
        }
    }
}