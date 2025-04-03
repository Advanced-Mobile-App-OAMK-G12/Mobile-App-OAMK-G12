package com.example.advancedandroidcourse.data.model

import com.google.firebase.Timestamp

data class Comment(
    val id: String = "",
    val content: String = "",
    val tipId: String = "",
    val userId: String = "",
    val timestamp: Timestamp? = null
)
