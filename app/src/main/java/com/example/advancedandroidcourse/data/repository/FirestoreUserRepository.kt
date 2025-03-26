package com.example.advancedandroidcourse.data.repository

import android.net.Uri
import com.example.advancedandroidcourse.data.model.Tip
import com.example.advancedandroidcourse.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    override suspend fun getCurrentUserProfile(): User? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = firestore.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    override suspend fun updateUserProfile(name: String, bio: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        val updates = mapOf("name" to name, "bio" to bio)
        firestore.collection("users").document(uid).update(updates).await()
        return true
    }

    override suspend fun uploadProfileImage(imageUri: Uri): String? {
        val uid = auth.currentUser?.uid ?: return null
        val ref = storage.reference.child("profile_images/$uid.jpg")
        ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString().also { url ->
            firestore.collection("users").document(uid).update("image", url)
        }
    }

    override suspend fun getUserFavorites(): List<Tip> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val favoriteSnapshots = firestore.collection("favorites")
            .whereEqualTo("userId", uid)
            .get()
            .await()

        val tipIds = favoriteSnapshots.documents.mapNotNull { it.getString("tipId") }

        val tips = mutableListOf<Tip>()
        for (tipId in tipIds) {
            val tipSnapshot = firestore.collection("tips").document(tipId).get().await()
            tipSnapshot.toObject(Tip::class.java)?.let { tips.add(it) }
        }
        return tips
    }

    override suspend fun getUserPosts(): List<Tip> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val tipSnapshots = firestore.collection("tips")
            .whereEqualTo("userId", uid)
            .get()
            .await()

        return tipSnapshots.documents.mapNotNull { it.toObject(Tip::class.java) }
    }
}
