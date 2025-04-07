package com.example.advancedandroidcourse.presentation.Favorite


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import com.example.advancedandroidcourse.data.model.Post
import com.example.advancedandroidcourse.data.repository.FavoriteTipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteTipsViewModel @Inject constructor(
    private val repository: FavoriteTipsRepository
) : ViewModel() {

    var tipList by mutableStateOf<List<Post>>(emptyList())
        private set

    init {
        fetchTips()
    }

    private fun fetchTips() {
        repository.getFavoriteTips { tips ->
            tipList = tips
        }
    }
}