package com.example.advancedandroidcourse.data.repository


import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import com.example.advancedandroidcourse.data.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

        val post = hashMapOf(
            "title" to title,
            "content" to content,
            "images" to (imageUrls ?: emptyList()),//If no images
            "userId" to userId,
            "userId" to userId,
            "savedCount" to 0,
            "tags" to tags,
            "timestamp" to System.currentTimeMillis()
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