package com.example.advancedandroidcourse.data.model

data class Tip(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val images: List<String> = emptyList(),
    val userId: String = "",
    val tags: List<String> = emptyList()
)
