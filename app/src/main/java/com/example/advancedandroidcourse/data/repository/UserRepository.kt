package com.example.advancedandroidcourse.data.repository

import android.net.Uri
import com.example.advancedandroidcourse.data.model.Tip
import com.example.advancedandroidcourse.data.model.User

interface UserRepository {
    suspend fun getCurrentUserProfile(): User?
    suspend fun updateUserProfile(name: String, bio: String): Boolean
    suspend fun uploadProfileImage(imageUri: Uri): String?
    suspend fun getUserFavorites(): List<Tip>
    suspend fun getUserPosts(): List<Tip>
}