package com.example.advancedandroidcourse.presentation.postDetails

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    init {
        Log.d("PostDetailsViewModel", "ViewModel created")
    }

    private val _postDetails = mutableStateOf<PostDetails?>(null)
    val postDetails: State<PostDetails?> = _postDetails

    fun getPostDetails(tipId: String) {
        Log.d("PostDetailsViewModel", "getPostDetails function STARTED with tipId: $tipId")
        viewModelScope.launch {
            Log.d("PostDetailsViewModel", "Fetching post details for tipId: $tipId")
            val result = postRepository.getPostDetails(tipId)
            Log.d("PostDetailsViewModel", "Post details fetched successfully for tipId: $tipId")
            _postDetails.value = result
        }

        Log.d("PostDetailsViewModel", "viewModelScope.launch has been triggered")
    }
}