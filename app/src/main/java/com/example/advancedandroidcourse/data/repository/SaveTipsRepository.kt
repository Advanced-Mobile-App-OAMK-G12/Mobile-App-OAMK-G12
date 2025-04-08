package com.example.advancedandroidcourse.data.repository

import android.util.Log
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.SavedTips
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SaveTipsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){

    suspend fun saveTip(tipId: String) {

        val userId = auth.currentUser?.uid ?: return

        val saved = SavedTips(
            tipId = tipId,
            userId = userId,
            timestamp = com.google.firebase.Timestamp.now()
        )

        firestore.collection("savedTips")
            .add(saved)
            .await()
    }

    suspend fun removeSavedTip(tipId: String) {
            val userId = auth.currentUser?.uid ?: return

            val querySnapshot = firestore.collection("savedTips")
                .whereEqualTo("tipId", tipId)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            for (doc in querySnapshot.documents) {
                firestore.collection("savedTips").document(doc.id).delete().await()
            }
    }

    suspend fun isSaved(tipId: String): Boolean {
            val userId = auth.currentUser?.uid ?: return false

            val querySnapshot = firestore.collection("savedTips")
                .whereEqualTo("tipId", tipId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            return !querySnapshot.isEmpty
    }

    suspend fun getSavedCount(tipId: String): Int {
            val snapshot = firestore.collection("savedTips")
                .whereEqualTo("tipId", tipId)
                .get()
                .await()
            return snapshot.size()

    }

    fun getSaveTips(onResult: (List<Post>) -> Unit) {
        firestore.collection("tips")
            .orderBy("savedCount", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val tips = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.takeIf { it.title != null }?.copy(id = doc.id)
                }
                onResult(tips)
            }
    }
}