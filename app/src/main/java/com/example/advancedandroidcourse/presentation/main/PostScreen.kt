package com.example.advancedandroidcourse.presentation.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.advancedandroidcourse.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    onBackClick: () -> Unit //Back navigation function
    //onPostClick: () -> Unit //Post action function
) {
    val postViewModel: PostViewModel = hiltViewModel() //inject ViewModel
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

    //Tag selection state
    val tagOptions = listOf(
        "essentials",
        "transportation",
        "official matters",
        "housing & jobs",
        "language & integration"
    )
    var selectedTag by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Back button
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        //Image selection-using placeholder and coil for loading image
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            imageUris.take(3).forEach { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = stringResource(id = R.string.add),
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(12.dp))
                    //.clickable {imagePickerLauncher.launch("image/*") } //open system file picker
                )

                //Spacer(modifier = Modifier.width(12.dp))
            }
            //Image picker button
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(12.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                //Text("+", fontSize = 24.sp, color = Color.DarkGray)
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = Color.DarkGray
                )
            }
        }

        if (showError && imageUris.isEmpty()) {
            Text("Please upload at least one image", color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Title input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.write_your_title_here)) },
            textStyle = TextStyle(fontSize = 18.sp),
            //placeholder = { Text(stringResource(id = R.string.write_your_title_here), color = Color.Gray) },
            modifier = Modifier.fillMaxWidth()
            //.background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
            //.padding(12.dp)
        )

        if (showError && title.isBlank()) {
            Text("Title cannot be empty", color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        //Content input
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text(stringResource(R.string.whats_your_tips)) },
            textStyle = TextStyle(fontSize = 16.sp),
            //placeholder = { Text(stringResource(id = R.string.whats_your_tips), color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
            //.background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
            //.padding(12.dp)
        )

        if (showError && content.isBlank()) {
            Text("Content cannot be empty", color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        //Dropdown for selecting a tag
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedTag,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select a #Tag") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                // trailingIcon = {
                //    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                //}
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tagOptions.forEach { tag ->
                    DropdownMenuItem(
                        text = { Text(tag) },
                        onClick = {
                            selectedTag = tag
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Save & post Buttons
        /*Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { /* save logic */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* save logic */ }) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = stringResource(id = R.string.save),
                        tint = Color.Gray
                    )
                }

                Text(
                    text = stringResource(id = R.string.save),
                    color = Color.Gray
                )
            }*/
        //Post Button
        Button(
            onClick = {
                if (title.isBlank() || content.isBlank() || imageUris.isEmpty()) {
                    showError = true
                    return@Button
                }
                isPosting = true
                showError = false

                postViewModel.createPost(
                    title = title,
                    content = content,
                    imageUris = imageUris,
                    tags = listOf(selectedTag)
                ) { success ->
                    isPosting = false
                    if (success) {
                        onBackClick()
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
