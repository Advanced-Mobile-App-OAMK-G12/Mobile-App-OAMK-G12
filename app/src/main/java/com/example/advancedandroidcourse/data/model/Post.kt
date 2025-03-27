package com.example.advancedandroidcourse.data.model

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrls: List<String> = emptyList(),
    val savedCount: Int = 0,
    val tags: List<String> = emptyList(),
    val timestamp: Timestamp? = null,
    val userId: String = ""
)
