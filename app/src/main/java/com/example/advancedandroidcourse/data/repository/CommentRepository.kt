package com.example.advancedandroidcourse.data.repository

import android.util.Log
import com.example.advancedandroidcourse.data.model.Comment
import com.example.advancedandroidcourse.data.model.CommentDetails
import com.example.advancedandroidcourse.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import java.sql.Time
import javax.inject.Inject

//data class commentRepository()
class CommentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun addComment(tipId: String, content: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val timestamp = Timestamp.now()

        val comment = hashMapOf(
            "content" to content,
            "tipId" to tipId,
            "userId" to userId,
            "timestamp" to timestamp
        )

        try {
            firestore.collection("comments")
                .add(comment)
                .await()
        } catch (e: Exception) {
            Log.e("CommentRepository", "Error adding comment: ${e.message}")
        }
    }

    suspend fun getComments(tipId: String): List<CommentDetails> {
        return try {
            val querySnapshot = firestore.collection("comments")
                .whereEqualTo("tipId", tipId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { document ->
                val data = document.data ?: return@mapNotNull null

                val comment = Comment(
                    id = document.id,
                    content = data["content"] as? String ?: "",
                    tipId = data["tipId"] as? String ?: "",
                    userId = data["userId"] as? String ?: "",
                    timestamp = data["timestamp"] as? Timestamp,
                )

                val userSnapshot = firestore.collection("users")
                    .document(comment.userId)
                    .get()
                    .await()
                val user = userSnapshot.toObject(User::class.java)

                CommentDetails(
                    comment = comment,
                    user = user ?: User(name = "Unknown", image = "")
                )
            }
        } catch (e: Exception) {
            Log.e("CommentRepository", "Error fetching comments: ${e.message}")
            return emptyList()
        }
    }

}