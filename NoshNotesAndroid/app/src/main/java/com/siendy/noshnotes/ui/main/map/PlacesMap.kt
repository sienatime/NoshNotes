package com.siendy.noshnotes.ui.main.map

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.databinding.MapFragmentBinding
import com.siendy.noshnotes.ui.UIConstants
import com.siendy.noshnotes.ui.main.MainUiState
import com.siendy.noshnotes.ui.main.MainViewModel
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun PlacesMap(
  mainViewModel: MainViewModel
) {
  val scope = rememberCoroutineScope()

  AndroidViewBinding(MapFragmentBinding::inflate) {
    val mapFragment = this.mapFragment.getFragment<SupportMapFragment>()
    mapFragment.getMapAsync { map ->
      scope.launch {
        mainViewModel.uiState.collect { mainUiState ->
          updateMapForState(mainUiState, map)
        }
      }
    }
  }
}

@SuppressLint("MissingPermission")
fun updateMapForState(mainUiState: MainUiState, map: GoogleMap?) {
  if (map == null) return

  map.isMyLocationEnabled = mainUiState.locationPermissionGranted
  map.uiSettings?.isMyLocationButtonEnabled = mainUiState.locationPermissionGranted

  val location = mainUiState.mapLocation

  map.moveCamera(
    CameraUpdateFactory.newLatLngZoom(
      LatLng(
        location.latitude,
        location.longitude
      ),
      UIConstants.DEFAULT_ZOOM
    )
  )

  updatePlacesOnMap(mainUiState.filteredPlaces, map)
}

private fun updatePlacesOnMap(places: List<Place>, map: GoogleMap) {
  map.clear()

  places.forEach { place ->
    val lat = place.latLong?.latitude
    val long = place.latLong?.longitude

    if (lat != null && long != null) {
      val latLong = LatLng(lat, long)

      map.addMarker(
        MarkerOptions()
          .title(place.name)
          .position(latLong)
          .snippet(place.address)
      )
    }
  }
}
