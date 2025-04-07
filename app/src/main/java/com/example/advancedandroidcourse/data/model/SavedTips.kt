package com.example.advancedandroidcourse.data.model

import com.google.firebase.Timestamp

data class SavedTips(
    val id: String = "",
    val tipId: String = "",
    val userId: String = "",
    val timestamp: Timestamp? = null
)
