package com.example.advancedandroidcourse.data.repository

import android.util.Log
import com.example.advancedandroidcourse.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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

//    Fetching Data
    fun getPosts(): Flow<List<Post>> = flow {
        try {
            val querySnapshot = firestore.collection("tips").get().await()
            Log.d("PostRepository", "Documents size: ${querySnapshot.documents.size}")
            val posts = querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Post::class.java)
            }
            emit(posts)
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching posts: ${e.message}")
            emit(emptyList<Post>())
        }
}


}