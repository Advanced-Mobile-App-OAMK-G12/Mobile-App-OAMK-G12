package com.example.advancedandroidcourse.presentation.postDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.data.model.Comment
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.presentation.main.PostViewModel

@Composable
fun PostDetailsScreen(
    tipId: String
) {
    Log.d("PostDetailsScreen", "Started with tipId: $tipId")

    val viewModel: PostDetailsViewModel = hiltViewModel()
    val postDetails = viewModel.postDetails.value

    LaunchedEffect(tipId) {
        Log.d("PostDetailsScreen", "Before calling viewModel.getPostDetails(tipId)")
        viewModel.getPostDetails(tipId)
        Log.d("PostDetailsScreen", "After calling viewModel.getPostDetails(tipId)")
    }

    if (postDetails != null) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberImagePainter(postDetails.post.images),
                contentDescription = "Post Image",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = postDetails.post.title,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = postDetails.post.content)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${postDetails.post.timestamp}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}