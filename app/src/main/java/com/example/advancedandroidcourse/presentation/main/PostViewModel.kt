package com.example.advancedandroidcourse.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

//    Save posts fetching from firestore
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    fun createPost(title: String, content: String, imageUrl: String?, onComplete: (Boolean) -> Unit) {

        viewModelScope.launch {
            postRepository.addPost(title, content, imageUrl, onComplete)
        }
    }

//    Fetching data
    fun getPosts() {
        viewModelScope.launch {
            postRepository.getPosts()
                .collect { postList ->
                    Log.d("PostViewModel", "Fetched posts: $postList")
                    _posts.value = postList
                }
        }
    }
}