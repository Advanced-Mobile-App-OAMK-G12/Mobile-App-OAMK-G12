package com.example.advancedandroidcourse.data.model

import com.google.firebase.Timestamp

data class Tip(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val userId: String = "",
    val tags: List<String> = emptyList(),
    val timestamp: Timestamp? = null
)
