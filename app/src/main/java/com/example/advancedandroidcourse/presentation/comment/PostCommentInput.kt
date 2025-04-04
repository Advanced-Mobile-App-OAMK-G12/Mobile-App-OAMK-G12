package com.example.advancedandroidcourse.presentation.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.R
import com.example.advancedandroidcourse.data.model.CommentDetails
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.presentation.postDetails.PostDetailsViewModel
import java.nio.file.WatchEvent

@Composable
fun PostCommentInput(
    tipId: String,
    onCommentAdded: () -> Unit,
) {

    var commentText by remember { mutableStateOf("") }
    val viewModel: PostDetailsViewModel = hiltViewModel()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            label = { Text(stringResource(R.string.commentPlaceHolder)) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged{ focusState ->
                    if (focusState.isFocused) {
                        keyboardController?.show()
                    }
                }
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = {
            if (commentText.isNotEmpty()) {
                viewModel.addComment(tipId, commentText)
                commentText = ""
                keyboardController?.hide()
                onCommentAdded()
            }
        },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post Comment")
        }
    }
}