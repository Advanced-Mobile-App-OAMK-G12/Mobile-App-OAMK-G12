package com.example.advancedandroidcourse.presentation.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.advancedandroidcourse.data.model.Tip
import com.example.advancedandroidcourse.presentation.search.SearchViewModel

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Black,
        modifier = Modifier.padding((8.dp))
    )
}

@Composable
fun TipItem(tip: Tip, navController: NavController, viewModel: SearchViewModel) {
    //Fetch author info
    val userProfiles by viewModel.userProfiles.collectAsState()
    val userInfo = userProfiles[tip.userId]

    //load if missing
    LaunchedEffect(tip.userId) {
        viewModel.loadUserInfoIfNeeded(tip.userId)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                Log.d("TipItem", "Clicked on tip: ${tip.id}")
                navController.navigate("postDetails/${tip.id}")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                if (tip.images.isNotEmpty()) {
                    AsyncImage(
                        model = tip.images.first(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text(
                        text = tip.title,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    tip.timestamp?.let {
                        Text(
                            text = it.formatToDate(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            //show author info under
            Spacer(modifier = Modifier.height(12.dp))

            if (userInfo != null) {
                AuthorInfo(
                    userAvatar = userInfo.first,
                    userName = userInfo.second,
                    isFavorited = false,
                    onToggleFavorited = {},
                    modifier = Modifier
                )
            } else {
                Text("Loading author info...", style = MaterialTheme.typography.bodySmall)
            }
        }

    }
}