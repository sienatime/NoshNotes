package com.siendy.noshnotes.ui.main

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.siendy.noshnotes.databinding.MapFragmentBinding
import com.siendy.noshnotes.ui.UIConstants.DEFAULT_ZOOM

@SuppressLint("MissingPermission")
@Composable
fun PlacesMap(
  mainViewModel: MainViewModel
) {
  val mainUiState by mainViewModel.uiState.collectAsState()

  AndroidViewBinding(MapFragmentBinding::inflate) {
    val mapFragment = this.mapFragment.getFragment<SupportMapFragment>()
    mapFragment.getMapAsync { map ->
      mainViewModel.onMapLoaded(map)

      map.isMyLocationEnabled = mainUiState.locationPermissionGranted
      map.uiSettings?.isMyLocationButtonEnabled = mainUiState.locationPermissionGranted

      val location = mainUiState.mapLocation

      map.moveCamera(
        CameraUpdateFactory.newLatLngZoom(
          LatLng(
            location.latitude,
            location.longitude
          ),
          DEFAULT_ZOOM
        )
      )
    }
  }
}
