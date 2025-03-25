package com.example.advancedandroidcourse.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    fun createPost(title: String, content: String, imageUrl: String?, onComplete: (Boolean) -> Unit) {

        viewModelScope.launch {
            postRepository.addPost(title, content, imageUrl, onComplete)
        }
    }
}