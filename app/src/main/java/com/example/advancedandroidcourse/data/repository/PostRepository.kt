package com.example.advancedandroidcourse.data.repository


import android.net.Uri
import android.util.Log
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import okhttp3.internal.isSensitiveHeader
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
   private val firestore: FirebaseFirestore,
   private val auth: FirebaseAuth,
   private val storage: FirebaseStorage

) {

    //Method to upload image to Firebase storage
    suspend fun uploadImageToStorage(imageUris: List< Uri>): List<String> {
        return try {
            val imageUrls = mutableListOf<String>()
            for (imageUri in imageUris) {
                //create a referance to Firebase storage
                //val storageRef = storage.reference
                val imageRef =
                    storage.reference.child("images/${UUID.randomUUID()}.jpg") //save as a unique file name
                imageRef.putFile(imageUri).await()
                val downloadUrl = imageRef.downloadUrl.await()
                imageUrls.add(downloadUrl.toString())
            }
            imageUrls
        } catch (e: Exception) {
            emptyList()
        }
    }


               /* //upload image
                val uploadTask = imageRef.putFile(imageUri)

                //wait for the upload to complete
                uploadTask.await()

                //Get download URL
                val downloadUrl = imageRef.downloadUrl.await()
                imageUrls.add(downloadUrl.toString())
            }
            imageUrls //Return list of image URLs

        } catch (e: Exception) {
            // Handle errors during upload
            emptyList()
        }
    }*/
    //Method to add post to Firestore
    fun addPost(title: String, content: String, imageUrls: List<String>?, tags: List<String>, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

    //Get current timestamp as a Timestamp object
        val timestamp = Timestamp(Date())

        val post = hashMapOf(
            "title" to title,
            "content" to content,
            "images" to (imageUrls ?: emptyList()),//If no images
            "userId" to userId,
            "savedCount" to 0,
            "tags" to tags,
            "timestamp" to timestamp
        )

        firestore.collection("tips")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d("FirestoreDebug", "Post added with ID: ${documentReference.id}")
                onComplete(true) }
            .addOnFailureListener{ e ->
                Log.e("FirestoreError", "Error adding post", e)
                onComplete(false) }
    }

//        Fetching LatestPosts
    suspend fun getLatestPosts(lastTimestamp: Timestamp?): List<PostDetails> {
        Log.d("PostRepository", "PostRepository Last timestamp: $lastTimestamp")

        return try {
            var query = firestore.collection("tips")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)

            if (lastTimestamp != null) {
                query = query.startAfter(lastTimestamp)
            }

            val querySnapshot = query.get().await()

            Log.d("PostRepository", "PostRepository Documents size: ${querySnapshot.documents.size}")

            querySnapshot.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java)?.copy(id = document.id)
                    ?: return@mapNotNull null
                Log.d("PostRepository", "PostRepository Post timestamp: ${post.timestamp}")
                val userSnapshot = firestore.collection("users").document(post.userId).get().await()
                val user = userSnapshot.toObject(User::class.java)

                PostDetails(
                    post = post,
                    user = user ?: User(name = "Unknown", image = ""),
                )
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "PostRepository Error fetching posts: ${e.message}")
            emptyList()
        }
    }

//    Get posts by savedCount
    suspend fun getHotPosts(): List<PostDetails> {
        return try {
            var query = firestore.collection("tips")
                .orderBy("savedCount", Query.Direction.DESCENDING)
                .limit(10)

            val posts = query.get().await()

            posts.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java)?.copy(id = document.id)
                    ?: return@mapNotNull null

                val userSnapshot = firestore.collection("users").document(post.userId).get().await()
                val user = userSnapshot.toObject(User::class.java)

                PostDetails(
                    post = post,
                    user = user ?: User(name = "Unknown", image = ""),
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

//    Update favoriteCount in Tips
    fun updateSavedCount(tipId: String, newSavedCount: Int, onComplete: (Boolean) -> Unit) {
        val postsRef = firestore.collection("tips").document(tipId)
        Log.d("PostRepository", "Updating saved count for post ID: $tipId")

        postsRef.update("savedCount", newSavedCount)
            .addOnSuccessListener {
                Log.d("PostRepository", "Successfully updated savedCount")
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                onComplete(false)
                Log.e("FirebaseError", "Error updating saved count", exception)
            }
    }

    suspend fun getPostDetails(tipId: String): PostDetails? {
        Log.d("PostRepository", "getPostDetails() STARTED with tipId: $tipId")

        return try {

            val postDoc = firestore.collection("tips").document(tipId).get().await()
            val post = postDoc.toObject(Post::class.java)?.copy(id = postDoc.id)

            val userDoc = firestore.collection("users").document(post?.userId ?: "").get().await()
            val user = userDoc.toObject(User::class.java)

            if (post != null && user != null) {
                Log.d("PostRepository", "Post and user found for tipId: $tipId")
                PostDetails(post = post, user = user)
            } else  {
                Log.d("PostRepository", "Post or user not found for tipId: $tipId")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching post details for tipId: $tipId", e)
            null
        }
    }

//    suspend fun getFavoriteCount(postId: String): Int {
//        try {
//            val postRef = firestore.collection("tips").document(postId)
//            val documentSnapshot = postRef.get().await()
//
//            return documentSnapshot.getLong("favoriteCount")?.toInt() ?: 0
//        } catch (e: Exception) {
//            Log.e("PostRepository", "Error fetching favoriteCount from Firestore: ${e.message}")
//            return 0
//        }
//    }
}