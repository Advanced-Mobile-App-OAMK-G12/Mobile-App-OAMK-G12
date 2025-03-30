package com.example.advancedandroidcourse.data.repository


import android.net.Uri
import android.util.Log
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
            .addOnSuccessListener { documentReerence ->
                Log.d("FirestoreDebug", "Post added with ID: ${documentReerence.id}")
                onComplete(true) }
            .addOnFailureListener{ e ->
                Log.e("FirestoreError", "Error adding post", e)
                onComplete(false) }
    }

//    Fetching PostDetails
    fun getPosts(): Flow<List<PostDetails>> = flow {
        try {
            val querySnapshot = firestore.collection("tips").get().await()
            Log.d("PostRepository", "Documents size: ${querySnapshot.documents.size}")
            val posts = querySnapshot.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java) ?: return@mapNotNull null
                val userSnapshot = firestore.collection("users").document(post.userId).get().await()
                val user = userSnapshot.toObject(User::class.java)

                PostDetails(
                    post = post,
                    userName = user?.name ?: "Unknown",
                    userAvatar = user?.image ?: ""
                )
            }
            emit(posts)
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching posts: ${e.message}")
            emit(emptyList<PostDetails>())
        }
    }

//    implement scrolling function in HomeScreen
    suspend fun getInitialPosts(): List<PostDetails> {
        try {
            val querySnapshot = firestore.collection("tips").limit(10).get().await()
            return querySnapshot.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java) ?: return@mapNotNull null
                val userSnapshot = firestore.collection("users").document(post.userId).get().await()
                val user = userSnapshot.toObject(User::class.java)

                PostDetails(
                    post = post,
                    userName = user?.name ?: "Unknown",
                    userAvatar = user?.image ?: ""
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }

//    Fetching more tips
    suspend fun getMorePosts(lastPostId: String?): List<PostDetails> {
        try {
            val querySnapshot = firestore.collection("tips")
                .startAt(lastPostId)
                .limit(10)
                .get()
                .await()

            return querySnapshot.documents.mapNotNull { document ->
                val post = document.toObject(Post::class.java) ?: return@mapNotNull null
                val userSnapshot = firestore.collection("users").document(post.userId).get().await()
                val user = userSnapshot.toObject(User::class.java)

                PostDetails(
                    post = post,
                    userName = user?.name ?: "Unknown",
                    userAvatar = user?.image ?: ""
                )
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }


}