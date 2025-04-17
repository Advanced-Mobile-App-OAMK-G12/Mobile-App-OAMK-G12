package com.example.advancedandroidcourse.data.repository

import android.util.Log
import com.example.advancedandroidcourse.data.model.Location
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRepository @Inject constructor(){

    val db = Firebase.firestore
    val locationRef = db.collection("Location")

    suspend fun addLocation(location: Location) : String {
        return try {
            val doc = locationRef.add(location).await()
            Log.d("PostDebug", "Firestore add success: ${doc.id}")
            doc.id
        } catch (e: Exception){
            Log.e("PostDebug", "Firestore add failed: ${e.message}")
            ""
        }
    }
}