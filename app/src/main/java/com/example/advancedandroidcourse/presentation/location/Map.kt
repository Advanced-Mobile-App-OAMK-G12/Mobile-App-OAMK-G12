package com.example.advancedandroidcourse.presentation.location

import android.util.Log
import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

@Composable
fun AddPostMapView(
    onLocationSelected: (LatLng, String, String) -> Unit
) {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    var selectedMarker by remember { mutableStateOf<Marker?>(null) }
    val geocoder = Geocoder(context, Locale.getDefault())

    AndroidView(
        factory = {
            mapView.getMapAsync { googleMap ->
//                Enable zoom
                googleMap.uiSettings.isZoomControlsEnabled = true
//                Enable drag
                googleMap.uiSettings.isScrollGesturesEnabled = true
                googleMap.uiSettings.isZoomGesturesEnabled = true

                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(64.9631, 25.7294), 6f
                    )
                )

                googleMap.setOnMapClickListener { latLng ->
                    selectedMarker?.remove()
                    selectedMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                    )
                    val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    val cityName = addresses?.firstOrNull()?.locality ?: "Unknown"
                    val addressLine = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown address"

                    Log.d("addressLine", "$addressLine")

                    onLocationSelected(latLng, cityName, addressLine)
                }
            }
            mapView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) { view ->
        mapView.onResume()
    }
}

