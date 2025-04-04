package com.example.advancedandroidcourse.data.model

fun Tip.toPost(): Post {
    return Post(
        id = this.id,
        title = this.title,
        content = this.content,
        images = this.images,
        userId = this.userId,
        tags = this.tags,
        savedCount = 0,
        timestamp = null
    )
}