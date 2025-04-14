package com.example.advancedandroidcourse.presentation.savedTips


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.PostDetails
import com.example.advancedandroidcourse.data.repository.SaveTipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveTipsViewModel @Inject constructor(
    private val repository: SaveTipsRepository
) : ViewModel() {

    var savedPosts by mutableStateOf<List<PostDetails>>(emptyList())
        private set

    init {
        fetchTips()
    }

    private fun fetchTips() {
        viewModelScope.launch {
            savedPosts = repository.getSavedTipsForCurrentUser()


        }
    }
}