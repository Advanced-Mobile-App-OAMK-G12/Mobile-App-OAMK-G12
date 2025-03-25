package com.example.advancedandroidcourse.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
   private val firestore: FirebaseFirestore,
   private val auth: FirebaseAuth
) {
    fun addPost(title: String, content: String, imageUrl: String?, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        val post = hashMapOf(
            "title" to title,
            "content" to content,
            "images" to listOfNotNull(imageUrl),
            "userId" to userId,
            "savedCount" to 0,
            "tags" to emptyList<String>(),
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("tips")
            .add(post)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener{ onComplete(false) }
    }
}