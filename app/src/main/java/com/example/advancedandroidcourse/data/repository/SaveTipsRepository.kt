package com.example.advancedandroidcourse.data.repository


import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.model.SavedTips
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.advancedandroidcourse.data.model.User
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SaveTipsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){

    suspend fun saveTip(tipId: String) {

        val userId = auth.currentUser?.uid ?: return

        val saved = SavedTips(
            tipId = tipId,
            userId = userId,
            timestamp = com.google.firebase.Timestamp.now()
        )

        firestore.collection("savedTips")
            .add(saved)
            .await()
    }

    suspend fun removeSavedTip(tipId: String) {
            val userId = auth.currentUser?.uid ?: return

            val querySnapshot = firestore.collection("savedTips")
                .whereEqualTo("tipId", tipId)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            for (doc in querySnapshot.documents) {
                firestore.collection("savedTips").document(doc.id).delete().await()
            }
    }

    suspend fun reSaveTip(postDetails: PostDetails) {
        val userId = auth.currentUser?.uid ?: return

        val saved = SavedTips(
            tipId = postDetails.post.id,
            userId = userId,
            timestamp = Timestamp.now()
        )

        firestore.collection("savedTips")
            .add(saved)
            .await()
    }

    suspend fun isSaved(tipId: String): Boolean {
            val userId = auth.currentUser?.uid ?: return false

            val querySnapshot = firestore.collection("savedTips")
                .whereEqualTo("tipId", tipId)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            return !querySnapshot.isEmpty
    }

    suspend fun getSavedCount(tipId: String): Int {
            val snapshot = firestore.collection("savedTips")
                .whereEqualTo("tipId", tipId)
                .get()
                .await()
            return snapshot.size()

    }

    //Get only the tips saved by the current user
    suspend fun getSavedTipsForCurrentUser(): List<PostDetails> {
        val userId = auth.currentUser?.uid ?: return emptyList()

        //Get all saved tip IDs for this user
        val savedTipsSnapshot = firestore.collection("savedTips")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val tipIds = savedTipsSnapshot.documents.mapNotNull { it.getString("tipId") }

        if (tipIds.isEmpty()) return emptyList()


        val tipsCollection = firestore.collection("tips")
        val usersCollection = firestore.collection("users")

        return tipIds.mapNotNull { tipId ->
            val tipDoc = tipsCollection.document(tipId).get().await()
            val post = tipDoc.toObject(Post::class.java)?.copy(id = tipDoc.id)

            post?.let {
                val userDoc = usersCollection.document(post.userId).get().await()
                val user = userDoc.toObject(User::class.java)
                if (user != null) PostDetails(post, user) else null
            }
        }

    }


}