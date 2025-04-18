package com.example.advancedandroidcourse.data.repository


import android.net.Uri
import android.util.Log
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.model.User
import com.example.advancedandroidcourse.data.model.Location
import com.google.api.QuotaLimit
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
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
                //create a reference to Firebase storage
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
    //Method to add post to Firestore
    fun addPost(
        title: String,
        content: String,
        imageUrls: List<String>?,
        tags: List<String>,
        locationId: String,
        onComplete: (Boolean) -> Unit
    ) {
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
            "timestamp" to timestamp,
            "locationId" to locationId
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
    suspend fun getLatestPosts(lastTimestamp: Timestamp?): Pair<List<PostDetails>, Timestamp?> {

        return try {
            var query = firestore.collection("tips")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)

            lastTimestamp?.let {
                query = query.startAfter(it)
            }

            val querySnapshot = query.get().await()

            Log.d("PostRepository", "PostRepository Documents size: ${querySnapshot.documents.size}")

            val posts = querySnapshot.documents.mapNotNull { document ->
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

            val nextPageKey = posts.lastOrNull()?.post?.timestamp
            posts to nextPageKey
        } catch (e: Exception) {
            Log.e("PostRepository", "PostRepository Error fetching posts: ${e.message}")
            emptyList<PostDetails>() to null
        }
    }

//    Get posts by savedCount
    suspend fun getHotPosts(
        lastSavedCount: Long?,
        lastDocId: String?
    ): Pair<List<PostDetails>, Pair<Long?, String?>> {
        return try {
            var query = firestore.collection("tips")
                .orderBy("savedCount", Query.Direction.DESCENDING)
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .limit(10)

            if (lastSavedCount != null && lastDocId != null) {
                query = query.startAfter(lastSavedCount, lastDocId)
            }

            val querySnapshot = query.get().await()

            Log.d("PostRepository", "Hot Documents size: ${querySnapshot.documents.size}")

            val posts = querySnapshot.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java)?.copy(id = document.id)
                    ?: return@mapNotNull null

                val userSnapshot = firestore.collection("users").document(post.userId).get().await()
                val user = userSnapshot.toObject(User::class.java)

                PostDetails(
                    post = post,
                    user = user ?: User(name = "Unknown", image = "")
                )
            }
            val lastPost = posts.lastOrNull()?.post
            posts to (lastPost?.savedCount?.toLong() to lastPost?.id)
        } catch (e: Exception) {
            emptyList<PostDetails>() to (null to null)
        }
    }

//    Get post randomly
suspend fun getRandomPosts(lastDocId: String? = null, limit: Long = 10): List<PostDetails> {
    return try {
        val postsQuery = firestore.collection("tips")
            .get()
            .await()

        val totalPosts = postsQuery.size()

        val randomIndexes = ( 0 until totalPosts).shuffled().take(limit.toInt())
        val randomPosts = randomIndexes.mapNotNull { index ->
            val postDoc = postsQuery.documents.getOrNull(index) ?: return@mapNotNull null
            val post = postDoc.toObject(Post::class.java)?.copy(id = postDoc.id)
                ?: return@mapNotNull null

            val userSnapshot = firestore.collection("users").document(post.userId).get().await()
            val user = userSnapshot.toObject(User::class.java)

            PostDetails(
                post = post,
                user = user ?: User(name = "Unknown", image = ""),
            )
        }
        randomPosts
    } catch (e: Exception) {
        emptyList<PostDetails>()
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

    suspend fun getLocationById(locationId: String): Location? {
        Log.d("PostRepository", "Fetching location for ID: $locationId")
        return try {
            val doc = firestore.collection("location")
                .document(locationId)
                .get()
                .await()
            Log.d("PostRepository", "Firestore doc exists: ${doc.exists()}")

            val location = doc.toObject(Location::class.java)
            Log.d("PostRepository", "Location fetched: $location")
            location
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getPostDetails(tipId: String): PostDetails? {
        Log.d("PostRepository", "getPostDetails() STARTED with tipId: $tipId")

        return try {

            val postDoc = firestore.collection("tips").document(tipId).get().await()
            val post = postDoc.toObject(Post::class.java)?.copy(id = postDoc.id)

            val userDoc = firestore.collection("users").document(post?.userId ?: "").get().await()
            val user = userDoc.toObject(User::class.java)

            val location = post?.locationId?.let { getLocationById(it) }
            Log.d("PostRepository", "Post locationId: ${post?.locationId}")

            if (post != null && user != null) {
                Log.d("PostRepository", "Post and user found for location: $location")
                PostDetails(post = post, user = user, location = location)
            } else  {
                Log.d("PostRepository", "Post or user not found for tipId: $tipId")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching post details for tipId: $tipId", e)
            null
        }
    }
}