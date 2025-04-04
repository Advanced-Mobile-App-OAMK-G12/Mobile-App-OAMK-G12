package com.example.advancedandroidcourse.presentation.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.repository.UserRepository
import com.example.advancedandroidcourse.data.model.Tip
import com.example.advancedandroidcourse.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // 用户信息状态
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    // 收藏的 tips
    private val _favoriteTips = MutableStateFlow<List<Tip>>(emptyList())
    val favoriteTips: StateFlow<List<Tip>> = _favoriteTips

    // 自己发布的 tips
    private val _postedTips = MutableStateFlow<List<Tip>>(emptyList())
    val postedTips: StateFlow<List<Tip>> = _postedTips

    // 加载状态（可选）
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // 错误状态（可选）
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadUserProfile()
        loadFavorites()
        loadPostedTips()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _user.value = userRepository.getCurrentUserProfile()
            } catch (e: Exception) {
                _error.value = "Failed to load user profile: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favoriteTips.value = userRepository.getUserFavorites()
            } catch (e: Exception) {
                _error.value = "Failed to load favorites: ${e.message}"
            }
        }
    }

    fun loadPostedTips() {
        viewModelScope.launch {
            try {
                _postedTips.value = userRepository.getUserPosts()
            } catch (e: Exception) {
                _error.value = "Failed to load posted tips: ${e.message}"
            }
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val imageUrl = userRepository.uploadProfileImage(imageUri)
                if (imageUrl != null) {
                    // 重新加载用户数据
                    loadUserProfile()
                }
            } catch (e: Exception) {
                _error.value = "Failed to upload image: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
    fun deletePost(postId: String) {
        viewModelScope.launch {
            try {
                userRepository.deletePost(postId)
                loadPostedTips()
            } catch (e: Exception) {
                _error.value = "Failed to delete post: ${e.message}"
            }
        }
    }
    fun updateProfile(name: String, bio: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val success = userRepository.updateUserProfile(name, bio)
                if (success) {
                    loadUserProfile()
                }
            } catch (e: Exception) {
                _error.value = "Failed to update profile: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}