package com.example.advancedandroidcourse.presentation

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TextField

@Composable
fun PostScreen(
    onBackClick: () -> Unit, //Back navigation function
    onPostClick: () -> Unit //Post action function
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        //Image selection-using placeholder and coil for loading image
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUri ?: "https://via.placeholder.com/80",
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
                    .clickable {/* open image picker */ }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { /* open image picker */ },
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 24.sp, color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        //Title input
        TextField(
            value = title,
            onValueChange = { title = it },
            textStyle = TextStyle(fontSize = 18.sp),
            placeholder = { Text("Write your title here...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Content input
        TextField(
            value = content,
            onValueChange = { content = it },
            textStyle = TextStyle(fontSize = 16.sp),
            placeholder = { Text("What's your tips...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Save & post Buttons
        Row(
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
                        contentDescription = "Save",
                        tint = Color.Gray
                    )
                }

                Text(
                    text = "Save",
                    color = Color.Gray
                )
            }
            Button(
                onClick = onPostClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "POST", color = Color.White)
            }
        }
    }
}