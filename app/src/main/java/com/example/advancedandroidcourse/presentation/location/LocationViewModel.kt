package com.example.advancedandroidcourse.presentation.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedandroidcourse.data.model.Location
import com.example.advancedandroidcourse.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    fun addLocation(
        location: Location,
        onResult: (String) -> Unit
    ) {
        Log.d("PostDebug", "LocationViewModel: addLocation called")
        viewModelScope.launch {
            val newId = repository.addLocation(location)
            Log.d("PostDebug", "LocationViewModel: location ID received: $newId")
            onResult(newId)
        }
    }
}