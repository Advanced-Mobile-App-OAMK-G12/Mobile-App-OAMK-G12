package com.example.advancedandroidcourse.data.model
import com.google.firebase.Timestamp

data class Notification(
    val comment: Comment,
    val commenter: User,
    val tipTitle: String,
    val tipImage: String,
    val timestamp: Timestamp? = comment.timestamp
)