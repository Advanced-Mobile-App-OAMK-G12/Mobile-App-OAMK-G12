package com.example.advancedandroidcourse.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.advancedandroidcourse.data.repository.PostRepository
import com.example.advancedandroidcourse.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    private val _hotTips = MutableStateFlow<List<String>>(emptyList())
    val hotTips: StateFlow<List<String>> = _hotTips

    private val _latestTips = MutableStateFlow<List<String>>(emptyList())
    val latestTips: StateFlow<List<String>> = _latestTips

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init {
        fetchTips()
    }

    private fun fetchTips() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            try {
                _hotTips.value = repository.getHotTips()
                _latestTips.value = repository.getLatestTips()
            } catch (e: Exception) {
                _errorMessage.value + "Failed to load tips"
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun searchTips(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""
            try {
                _searchResults.value = if (query.isNotEmpty()) repository.searchTips(query) else emptyList()
            } catch (e: Exception) {
                _errorMessage.value = "Search failed"
            } finally {
                _isLoading.value = false
            }

        }
    }
}