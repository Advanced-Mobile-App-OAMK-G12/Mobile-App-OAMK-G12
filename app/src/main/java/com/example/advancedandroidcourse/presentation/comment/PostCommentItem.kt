package com.example.advancedandroidcourse.presentation.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.advancedandroidcourse.data.model.CommentDetails
import com.example.advancedandroidcourse.presentation.composables.formatToDate
import com.example.advancedandroidcourse.ui.theme.mainTextColor
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CommentItem(commentDetails: CommentDetails) {
    val comment = commentDetails.comment
    val user = commentDetails.user

    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // User Avatar
        Image(
            painter = rememberImagePainter(user.image),
            contentDescription = "User Avatar",
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            // User Name
            Text(text = user.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Comment content
            Text(text = comment.content)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment.timestamp.formatToDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.mainTextColor
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
