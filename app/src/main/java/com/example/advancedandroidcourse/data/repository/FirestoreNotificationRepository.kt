package com.example.advancedandroidcourse.data.repository

import com.example.advancedandroidcourse.data.model.Notification
import com.example.advancedandroidcourse.data.model.toNotification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreNotificationRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val commentRepository: CommentRepository
) : NotificationRepository {

    override suspend fun getNotificationsForUser(userId: String): List<Notification> {
        val notifications = mutableListOf<Notification>()


        val userTipsSnapshot = firestore.collection("tips")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val userTips = userTipsSnapshot.documents.mapNotNull { document ->
            val tipId = document.id
            val tipTitle = document.getString("title") ?: "Untitled"
            val tipImages = document.get("images") as? List<String> ?: emptyList()
            Triple(tipId, tipTitle, tipImages.firstOrNull().orEmpty())
        }


        for ((tipId, tipTitle, tipImage) in userTips) {
            val commentDetails = commentRepository.getComments(tipId)
            commentDetails.forEach { commentDetail ->
                notifications.add(commentDetail.toNotification(tipTitle, tipImage))
            }
        }


        return notifications.sortedByDescending { it.timestamp }
    }
}
