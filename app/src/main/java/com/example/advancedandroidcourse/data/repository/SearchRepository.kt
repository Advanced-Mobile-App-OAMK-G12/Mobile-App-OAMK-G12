package com.example.advancedandroidcourse.data.repository

import com.example.advancedandroidcourse.data.model.Tip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getLatestTips(): List<Tip> {
        return try {
            db.collection("tips")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()
                .documents
                .map {
                    val id = it.id
                    val title = it.getString("title") ?: ""
                    Tip(id, title)
                }
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
                    val id = doc.id
                    val title = doc.getString("title") ?: ""
                    val savedCount = doc.getLong("savedCount") ?: 0
                    if (savedCount > 0) Tip(id, title) else null
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchTips(query: String): List<Tip> {
        return try {
            db.collection("tips")
                .orderBy("title")
                .startAt(query)
                .endAt( query + '\uf8ff')
                .get()
                .await()
                .documents
                .map {
                    val id = it.id
                    val title = it.getString("title") ?: ""
                    Tip(id, title)}

        } catch (e: Exception) {
            emptyList()
        }
    }
}