package com.example.advancedandroidcourse.presentation.location

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

@Composable
fun AddPostMapView(
    onLocationSelected: (LatLng) -> Unit
) {
    val context = LocalContext.current
//    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
//    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }


    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
//            onCreate(null)
//            getMapAsync {
//                googleMap = it
//                it.uiSettings.isZoomControlsEnabled = true
//                it.moveCamera(
//                    CameraUpdateFactory.newLatLngZoom(
//                        LatLng(65.0121, 25.4651), 12f
//                    )
//                )
//                it.setOnMapClickListener { latLng ->
//                    selectedLatLng = latLng
//                    onLocationSelected(latLng)
//                    it.clear()
//                }
//            }
        }
    }

    var selectedMarker by remember { mutableStateOf<Marker?>(null) }

    AndroidView(
        factory = {
            mapView.getMapAsync { googleMap ->
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(65.0121, 25.4651), 12f
                    )
                )

                googleMap.setOnMapClickListener { latLng ->
                    selectedMarker?.remove()

                    selectedMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                    )

                    onLocationSelected(latLng)
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

