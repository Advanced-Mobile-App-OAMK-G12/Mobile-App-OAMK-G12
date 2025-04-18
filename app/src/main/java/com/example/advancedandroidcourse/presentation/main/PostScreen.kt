package com.example.advancedandroidcourse.presentation.main

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.data.model.Location
import com.example.advancedandroidcourse.presentation.location.AddPostMapView
import com.example.advancedandroidcourse.presentation.location.LocationViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.model.LatLng


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    navController: NavController,
    onBackClick: () -> Unit //Back navigation function
) {
    val postViewModel: PostViewModel = hiltViewModel() //inject ViewModel
//    Inject LocationViewModel
    val locationViewModel: LocationViewModel = hiltViewModel()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isPosting by remember { mutableStateOf(false) } // Track post status
    var showError by remember { mutableStateOf(false) } //Track validation errors

    //Create an image picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        imageUris = uris ?: emptyList()
    }

//    For map
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedCity by remember { mutableStateOf("") }
    var selectedAddress by remember { mutableStateOf("") }

    //Tag selection state
    val tagOptions = listOf(
        "essentials",
        "transportation",
        "official matters",
        "housing & jobs",
        "language & integration"
    )
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues (bottom = 16.dp)
    ) {
        item {
            //Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }

        item {

            //Image selection-using placeholder and coil for loading image
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                imageUris.take(3).forEach { uri ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = stringResource(id = R.string.add),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray)
                        )
                        IconButton(
                            onClick = { imageUris = imageUris - uri     },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                //Image picker button
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add),
                        tint = Color.DarkGray
                    )
                }
            }
        }


        item {
            if (showError && imageUris.isEmpty()) {
                Text("Please upload at least one image", color = Color.Red, fontSize = 14.sp)
            }
        }

        item {

            //Title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.write_your_title_here)) },
                textStyle = TextStyle(fontSize = 18.sp),
                modifier = Modifier.fillMaxWidth()
            )

            if (showError && title.isBlank()) {
                Text("Title cannot be empty", color = Color.Red, fontSize = 14.sp)
            }
        }

        item {

            //Content input
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.whats_your_tips)) },
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )


            if (showError && content.isBlank()) {
                Text("Content cannot be empty", color = Color.Red, fontSize = 14.sp)
            }
        }

        item {

            //Tag Selection using clickable buttons
            Text("Select # Tags", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                modifier = Modifier.fillMaxWidth(),
                //verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tagOptions.forEach { tag ->
                    FilterChip(
                        selected = selectedTags.contains(tag),
                        onClick = {
                            selectedTags = if (selectedTags.contains(tag)) {
                                selectedTags - tag
                            } else {
                                selectedTags + tag
                            }
                        },
                        label = { Text(tag) },
                        modifier = Modifier
                            .defaultMinSize(minWidth = 48.dp)
                            //.padding(4.dp)
                            //.widthIn(min = 170.dp)
                    )
                }
            }
        }

//        Google Map
        item {
            Text("Select Location", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            AddPostMapView { latLng, city, address ->
                selectedLocation = latLng
                selectedCity = city
                selectedAddress = address
            }
        }

        item {
            //Post Button
            Button(
                onClick = {
                    Log.d("PostDebug", "Post button clicked")
                    if (title.isBlank() || content.isBlank() || imageUris.isEmpty()) {
                        showError = true
                        return@Button
                    }
                    isPosting = true
                    showError = false

//                    Fetch latitude and longitude
                    if (selectedLocation != null) {
                        //val latitude = selectedLocation!!.latitude
                        //val longitude = selectedLocation!!.longitude
                        //val cityName = selectedCity
                        //val address = selectedAddress

                       //Log.d("PostDebug", "Location selected: $latitude, $longitude")

                        val location = Location(
                            city = selectedCity,
                            address = selectedAddress,
                            latitude = selectedLocation!!.latitude,
                            longitude = selectedLocation!!.longitude
                        )

                        locationViewModel.addLocation(location) { newLocationId ->
                            Log.d("PostDebug", "Location created with ID: $newLocationId")
                            if (newLocationId.isNotEmpty()) {
                                postViewModel.createPost(
                                    title = title,
                                    content = content,
                                    imageUris = imageUris,
                                    tags = selectedTags.toList(),
                                    locationId = newLocationId,
                                ) { success ->
                                    isPosting = false
                                    if (success) {
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            } else {
                                isPosting = false
                            }
                        }
                    } else {
                        //No location Selected
                        postViewModel.createPost(
                            title = title,
                            content = content,
                            imageUris = imageUris,
                            tags = selectedTags.toList(),
                            locationId = "",
                        ) { success ->
                            isPosting = false
                            if (success) {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                },
                enabled = !isPosting, //Disable when posting
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (isPosting) {
                    Text("Posting...", color = Color.Blue)
                } else {
                    Text(text = stringResource(id = R.string.post), color = Color.White)
                }
            }
        }
    }
}
