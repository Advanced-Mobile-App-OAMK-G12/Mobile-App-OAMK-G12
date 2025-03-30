package com.example.advancedandroidcourse.presentation.main


import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {


//    Save posts fetching from firestore
    private val _posts = MutableStateFlow<List<PostDetails>>(emptyList())
    val posts: StateFlow<List<PostDetails>> get() = _posts
//    Getting more posts
    private var lastPostId: String? = null
    private var isLoading = false

    fun createPost(title: String, content: String, imageUris: List<Uri>?, tags: List<String>, onComplete: (Boolean) -> Unit) {


        viewModelScope.launch {
            if (!title.isNullOrBlank() && !content.isNullOrBlank()) {
                //upload image to Firebase storage
                val imageUrls = if (imageUris != null && imageUris.isNotEmpty()) {
                    postRepository.uploadImageToStorage(imageUris)
                } else {
                    emptyList()
                }

                //if no image is selected,create post without image
                postRepository.addPost(title, content, imageUrls, tags, onComplete)
            } else {
                onComplete(false)
            }

        }
    }

//    Fetching data
    fun getPosts() {
        viewModelScope.launch {
            val newPosts = postRepository.getInitialPosts()
            if (newPosts.isNotEmpty()) {
                Log.d("PostViewModel", "Fetched posts: $newPosts")
                lastPostId = newPosts.last().post.id
            }

            _posts.value = newPosts

        }
    }

//    Getting more posts
    fun getMorePosts() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            val newPosts = postRepository.getMorePosts(lastPostId)
            if (newPosts.isNotEmpty()) {
                lastPostId = newPosts.last().post.id
                _posts.value = _posts.value + newPosts
            }
            isLoading = false
        }
    }
}