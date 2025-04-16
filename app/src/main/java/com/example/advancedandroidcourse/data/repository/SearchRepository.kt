package com.example.advancedandroidcourse.data.repository

import com.example.advancedandroidcourse.data.model.Tip
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    private fun getTipFromDoc(doc: DocumentSnapshot): Tip {
        val id = doc.id
        val title = doc.getString("title") ?: ""
        val content = doc.getString("content") ?: ""
        val images = doc.get("images") as? List<String> ?: emptyList()
        val userId = doc.getString("userId") ?: ""
        val tags = doc.get("tags") as? List<String> ?: emptyList()
        val timestamp = doc.getTimestamp("timestamp")
        return Tip(id, title, content, images, userId, tags, timestamp)
    }

    suspend fun getLatestTips(): List<Tip> {
        return try {
            db.collection("tips")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()
                .documents
                .map { getTipFromDoc(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHotTips(): List<Tip> {
        return try {
            db.collection("tips")
                .orderBy("savedCount", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    val savedCount = doc.getLong("savedCount") ?: 0
                    if (savedCount > 0) getTipFromDoc(doc) else null
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchTips(query: String): List<Tip> {
        return try {
            val result = db.collection("tips")
                .get()
                .await()
                .documents
                .map { getTipFromDoc(it) }

            //Filter results client-side using Lowercase comparison
            result.filter { it.title.contains(query, ignoreCase = true) }

        } catch (e: Exception) {
            emptyList()
        }
    }
}