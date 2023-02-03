package com.siendy.noshnotes.ui.main.map

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.databinding.MapFragmentBinding
import com.siendy.noshnotes.ui.UIConstants
import com.siendy.noshnotes.ui.main.MainUiState
import com.siendy.noshnotes.ui.main.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.max

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

  updatePlacesOnMap(
    mainUiState.filteredPlaces,
    map,
    mainUiState.defaultMapLocation
  )
}

private fun updatePlacesOnMap(
  places: List<Place>,
  map: GoogleMap,
  defaultLocation: LatLng
) {
  map.clear()

  if (places.isEmpty()) {
    map.moveCamera(
      CameraUpdateFactory.newLatLngZoom(
        defaultLocation,
        UIConstants.DEFAULT_ZOOM
      )
    )
    return
  }

  val boundsBuilder = LatLngBounds.Builder()

  places.forEach { place ->
    val lat = place.latLong?.latitude
    val long = place.latLong?.longitude

    if (lat != null && long != null) {
      val latLong = LatLng(lat, long)
      boundsBuilder.include(latLong)

      map.addMarker(
        MarkerOptions()
          .title(place.name)
          .position(latLong)
          .snippet(place.address)
      )
    }
  }
  val bounds = boundsBuilder.build()

  map.setLatLngBoundsForCameraTarget(bounds)
  map.moveCamera(
    CameraUpdateFactory.newLatLngZoom(
      bounds.center,
      getZoomLevel(bounds)
    )
  )
}

/**
 * Calculate the zoom level based on how far apart the markers are.
 * This is super magic numbery but just experimented with what looked good.
 */
private fun getZoomLevel(bounds: LatLngBounds): Float {
  val northEastBound = bounds.northeast
  val southWestBound = bounds.southwest

  val latDiff = (southWestBound.latitude - northEastBound.latitude).absoluteValue
  val longDiff = (northEastBound.longitude - southWestBound.longitude).absoluteValue
  val maxDiff = max(latDiff, longDiff)

  // each zoom level is about this far apart
  val step = 0.06

  return if (maxDiff < 0.02) {
    UIConstants.DEFAULT_ZOOM
  } else {
    UIConstants.DEFAULT_ZOOM - (maxDiff / step).toFloat()
  }
}
