package com.example.advancedandroidcourse.presentation.main


import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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

//    Update savedCount
    fun updateSavedCount(tipId: String, newSavedCount: Int) {
        Log.d("PostViewModel", "PostViewModel Updating saved count for post ID: $tipId with new value: $newSavedCount")

        postRepository.updateSavedCount(tipId, newSavedCount) { success ->
            if (success) {
                val updatedList = _posts.value.map {
                    if (it.post.id == tipId) {
                        it.copy(post = it.post.copy(savedCount = newSavedCount))
                    } else  it
                }
                _posts.value = updatedList
            }
        }
    }

//    Get postDetails
    private val _postDetails = mutableStateOf<PostDetails?>(null)
    val postDetails: State<PostDetails?> = _postDetails

    fun getPostDetails(tipId: String) {
        viewModelScope.launch {
            Log.d("PostViewModel", "Fetching post details for tipId: $tipId")
            val result = postRepository.getPostDetails(tipId)
            Log.d("PostViewModel", "Post details fetched successfully for tipId: $tipId")
            _postDetails.value = result
        }
    }
}