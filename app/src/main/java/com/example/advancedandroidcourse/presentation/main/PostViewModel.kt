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
import com.example.advancedandroidcourse.data.repository.SaveTipsRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val savedTipsRepository: SaveTipsRepository
) : ViewModel() {


//    Save posts fetching from firestore
    private val _posts = MutableStateFlow<List<PostDetails>>(emptyList())
    val posts: StateFlow<List<PostDetails>> = _posts.asStateFlow()

//    Fetch SavedCount
    private val _savedCount = mutableStateOf(0)
    val savedCount: State<Int> = _savedCount

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

//Get LatestPosts
    fun loadLatestPosts() {
        viewModelScope.launch {
            val posts = postRepository.getLatestPosts(null)
            _posts.value = posts
        }
    }

//    Load tips by savedCount
    fun loadHotPosts() {
        viewModelScope.launch {
            val hotPosts = postRepository.getHotPosts()
            _posts.value = hotPosts
        }
    }

//    Lost RandomPosts
fun loadRandomPosts() {
    viewModelScope.launch {
        val randomPosts = postRepository.getRandomPosts()
        _posts.value = randomPosts
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

//    Fetch savedTipCount
//    fun fetchSavedCount(tipId: String) {
//        viewModelScope.launch {
//            _savedTipCount.value = savedTipsRepository.getSavedCount(tipId)
//        }
//    }
}