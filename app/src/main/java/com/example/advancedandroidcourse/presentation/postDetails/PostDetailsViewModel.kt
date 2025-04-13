package com.example.advancedandroidcourse.presentation.postDetails

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.CommentDetails
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.repository.CommentRepository
import com.example.advancedandroidcourse.data.repository.PostRepository
import com.example.advancedandroidcourse.data.repository.SaveTipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val savedTipsRepository: SaveTipsRepository
): ViewModel() {

    private val _postDetails = mutableStateOf<PostDetails?>(null)
    val postDetails: State<PostDetails?> = _postDetails

    private val _comments = MutableStateFlow<List<CommentDetails>>(emptyList())
    val comments: StateFlow<List<CommentDetails>> = _comments.asStateFlow()

    private val _isSaved = mutableStateOf(false)
    val isSaved: State<Boolean> = _isSaved

    private val _savedCount = mutableStateOf(0)
    val savedCount: State<Int> = _savedCount

    private val _commentCount = mutableStateOf(0)
    val commentCount: State<Int> = _commentCount

    fun getPostDetails(tipId: String) {

        viewModelScope.launch {
            val result = postRepository.getPostDetails(tipId)
            _postDetails.value = result
        }

        Log.d("PostDetailsViewModel", "viewModelScope.launch has been triggered")
    }

    fun addComment(tipId: String, content: String) {
        viewModelScope.launch {
            commentRepository.addComment(tipId, content)
            getComments(tipId)
        }
    }

    fun getComments(tipId: String) {
        viewModelScope.launch {
            try {
                val fetchedComments = commentRepository.getComments(tipId)
                Log.d("PostDetailViewModel", "Fetched ${fetchedComments.size} comments")
                _comments.value = fetchedComments
            } catch (e: Exception) {
                Log.e("PostDetailViewModel", "Error fetching comments", e)
            }
        }
    }

//    fun fetchFavoriteCount(tipId: String) {
//        viewModelScope.launch {
//            try {
//                val favoriteCount = postRepository.getFavoriteCount(tipId)
//
//                val current = _postDetails.value
//                if (current != null && current.post.id == tipId) {
//                    _postDetails.value = current.copy(post = current.post.copy(savedCount = favoriteCount))
//                }
//            } catch (e: Exception) {
//                Log.e("PostDetailsViewModel", "Error fetching favoriteCount: ")
//            }
//        }
//    }

    fun updateFavoriteCount(postId: String, newCount: Int) {
        Log.d("PostDetailsViewModel", "Updating favorite count: $newCount for postId=$postId")
        postRepository.updateSavedCount(postId, newCount) { success ->
            if (success) {
                val current = _postDetails.value
                if (current != null && current.post.id == postId) {
                    _postDetails.value = current.copy(post = current.post.copy(savedCount = newCount))
                }
            }
        }
    }

    fun toggleSaveTip(tipId: String) {
        viewModelScope.launch {
            if (_isSaved.value) {
                savedTipsRepository.removeSavedTip(tipId)
                _isSaved.value = false
            } else {
                savedTipsRepository.saveTip(tipId)
                _isSaved.value = true
            }

            fetchSavedCount(tipId)
        }
    }

    fun checkIfSaved(tipId: String) {
        viewModelScope.launch {
            try {
                _isSaved.value = savedTipsRepository.isSaved(tipId)
            } catch (e: Exception) {
                Log.e("PostDetailsViewModel", "checkIfSaved error: ${e.message}")
            }
        }
    }

    fun fetchSavedCount(tipId: String) {
        viewModelScope.launch {
            _savedCount.value = savedTipsRepository.getSavedCount(tipId)
        }
    }

    fun fetchCommentCount(tipId: String) {
        viewModelScope.launch {
            _commentCount.value = commentRepository.fetchCommentCount(tipId)
        }
    }
}