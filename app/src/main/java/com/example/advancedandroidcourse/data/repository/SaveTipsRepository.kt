package com.example.advancedandroidcourse.data.repository

import com.example.advancedandroidcourse.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class SaveTipsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
){
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