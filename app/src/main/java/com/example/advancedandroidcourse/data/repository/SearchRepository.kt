package com.example.advancedandroidcourse.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getLatestTips(): List<String> {
        return try {
            db.collection("tips")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()
                .documents
                .map { it.getString("title") ?: ""}
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHotTips(): List<String> {
        return try {
            db.collection("tips")
                .orderBy("savedCount", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()
                .documents
                .map { it.getString("title") ?: ""}
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchTips(query: String): List<String> {
        return try {
            db.collection("tips")
                .orderBy("title")
                .startAt(query)
                .endAt( query + '\uf8ff')
                .get()
                .await()
                .documents
                .map { it.getString("title") ?: ""}

        } catch (e: Exception) {
            emptyList()
        }
    }
}