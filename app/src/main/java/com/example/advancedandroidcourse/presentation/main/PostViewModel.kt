package com.example.advancedandroidcourse.presentation.main


import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.repository.PostRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {


//    Save posts fetching from firestore
    private val _posts = MutableStateFlow<List<PostDetails>>(emptyList())
    val posts: StateFlow<List<PostDetails>> = _posts.asStateFlow()
//    Getting more posts
    private var lastTimestamp: Timestamp? = null
//    private var isLoading = false

    init {
        getInitialPosts()
    }

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


//    Initial Posts
    fun getInitialPosts() {
        viewModelScope.launch {
            try {
                val newPosts = postRepository.getInitialPosts()
                if (newPosts.isNotEmpty()) {
                    lastTimestamp = newPosts.last().post.timestamp
                    Log.d("PostViewModel", "PostViewModel Fetched initial posts: ${newPosts.size}")
                    _posts.value = newPosts
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "PostViewModel Error fetching initial posts", e)
            }
        }

    }

    //    Fetching data
    fun getPosts() {
        Log.d("PostViewModel", "Calling getPosts()")
        // If lastTimestamp is null, it means we need to load initial posts
        if (lastTimestamp == null) {
            getInitialPosts()
            return
        }

        viewModelScope.launch {
            try {
                val morePosts = postRepository.getPosts(lastTimestamp!!)
                Log.d("PostViewModel", "Fetched more posts: ${morePosts.size}")

                if (morePosts.isNotEmpty()) {
                    lastTimestamp = morePosts.last().post.timestamp
                    _posts.value = _posts.value + morePosts
                } else {
                    Log.d("PostViewModel", "No more posts available.")
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching more posts", e)
            }
        }
    }
}

//    Getting more posts
//    fun getMorePosts() {
//        if (isLoading) return
//        isLoading = true
//
//        viewModelScope.launch {
//            val lastTimestamp = _posts.value.lastOrNull()?.post?.timestamp
//            Log.d("PostViewModel", "PostViewModel getMorePosts() called. Last timestamp: $lastTimestamp")
//            try {
//                val morePosts = postRepository.getMorePosts(lastTimestamp)
//                Log.d("PostViewModel", "PostViewModel More posts fetched: ${morePosts.size}")
//
//                if (morePosts.isNotEmpty()) {
//                    _posts.value = _posts.value + morePosts
//                    Log.d("PostViewModel", "PostViewModel Total posts after update: ${_posts.value.size}")
//                }
//            } catch (e: Exception) {
//                Log.e("PostViewModel", "PostViewModel Error fetching more posts", e)
//            }
//        }
//    }
//}