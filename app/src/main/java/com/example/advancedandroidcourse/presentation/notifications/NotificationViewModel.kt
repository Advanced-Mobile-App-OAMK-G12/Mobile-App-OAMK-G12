package com.example.advancedandroidcourse.presentation.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.Notification
import com.example.advancedandroidcourse.data.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _hasUnreadNotifications = MutableStateFlow(false)
    val hasUnreadNotifications: StateFlow<Boolean> = _hasUnreadNotifications

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = notificationRepository.getNotificationsForUser(userId)

                Log.d("DEBUG", "Loaded notifications: $result")
                _notifications.value = result

                // 获取用户 lastViewedNotifications 时间戳
                val snapshot = firestore.collection("users").document(userId).get().await()
                val lastViewed = snapshot.getTimestamp("lastViewedNotifications")

                // 如果有任一通知比该时间晚，则为未读
                val hasNew = result.any { it.timestamp != null && lastViewed != null && it.timestamp!! > lastViewed }
                _hasUnreadNotifications.value = hasNew
            } catch (e: Exception) {
                _error.value = "Failed to load notifications: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun markNotificationsAsRead() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            firestore.collection("users").document(userId)
                .update("lastViewedNotifications", com.google.firebase.Timestamp.now())
            _hasUnreadNotifications.value = false
        }
    }
}
