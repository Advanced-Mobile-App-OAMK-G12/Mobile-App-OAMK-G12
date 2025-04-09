package com.example.advancedandroidcourse.presentation.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.model.Tip
import com.example.advancedandroidcourse.data.model.toPost
import com.example.advancedandroidcourse.presentation.auth.AuthState
import com.example.advancedandroidcourse.presentation.auth.AuthViewModel
import com.example.advancedandroidcourse.presentation.composables.BottomBar
import com.example.advancedandroidcourse.presentation.composables.PostItem
import com.example.advancedandroidcourse.presentation.notifications.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    val user by viewModel.user.collectAsState()
    val posts by viewModel.postedTips.collectAsState()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.uploadProfileImage(it) }
    }
    val authState = authViewModel.authState.observeAsState()
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val hasUnreadNotifications by notificationViewModel.hasUnreadNotifications.collectAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(64.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { authViewModel.signout() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sign out",
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController, hasUnreadNotifications) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Profile Image
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
            ) {
                if (!user?.image.isNullOrBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(user?.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // or Icons.Default.Add
                            contentDescription = "Add Image"
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            Text(text = user?.name ?: "Username", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(12.dp))
            // Bio + Edit in one line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = user?.bio ?: "No bio",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = { navController.navigate("editProfile") },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(16.dp) // 控制图标大小
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Posts tab title with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PostAdd,
                    contentDescription = "My Posts",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Posts", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Posts Grid
            if (posts.isEmpty()) {
                Text("No tips to show.")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(bottom = 64.dp)
                ) {
                    items(posts.size) { index ->
                        val tip = posts[index]
                        user?.let { currentUser ->
                            val postDetails = PostDetails(post = tip.toPost(), user = currentUser)
                            MyPostItem(
                                postDetails = postDetails,
                                navController = navController,
                                onDelete = { viewModel.deletePost(tip.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

    /*@Composable
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
    }*/

@Composable
fun MyPostItem(
    postDetails: PostDetails,
    navController: NavController,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
    ) {
        PostItem(
            postDetails = postDetails,
            showAuthorInfo = false,
            onToggleFavorited = {},
            navController = navController
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
                .size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Tip",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}