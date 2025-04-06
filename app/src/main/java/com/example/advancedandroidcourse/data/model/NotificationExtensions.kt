package com.example.advancedandroidcourse.data.model

fun CommentDetails.toNotification(
    tipTitle: String,
    tipImage: String
): Notification {
    return Notification(
        comment = this.comment,
        commenter = this.user,
        tipTitle = tipTitle,
        tipImage = tipImage,
        timestamp = this.comment.timestamp
    )
}
