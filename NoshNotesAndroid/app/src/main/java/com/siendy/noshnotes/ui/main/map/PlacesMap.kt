package com.siendy.noshnotes.ui.main.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.UIConstants
import com.siendy.noshnotes.ui.components.FullScreenLoading
import com.siendy.noshnotes.ui.main.MainViewModel
import kotlin.math.absoluteValue
import kotlin.math.max

@Composable
fun PlacesMap(
  mainViewModel: MainViewModel
) {
  val mainUiState by mainViewModel.uiState.collectAsState()

  val cameraPositionState = rememberCameraPositionState()

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    cameraPositionState = cameraPositionState,
    uiSettings = MapUiSettings(
      myLocationButtonEnabled = mainUiState.locationPermissionGranted,
    ),
    properties = MapProperties(
      isMyLocationEnabled = mainUiState.locationPermissionGranted
    )
  ) {
    val places = mainUiState.filteredPlaces

    cameraPositionState.position = if (places.isEmpty()) {
      CameraPosition.fromLatLngZoom(
        mainUiState.defaultMapLocation, UIConstants.DEFAULT_ZOOM
      )
    } else {
      mapMarkers(places)
    }
  }

  if (mainUiState.loading) {
    FullScreenLoading()
  }
}

@Composable
private fun mapMarkers(places: List<Place>): CameraPosition {
  val boundsBuilder = LatLngBounds.Builder()

  places.forEach { place ->
    placeToLatLng(place)?.let { latLong ->
      boundsBuilder.include(latLong)

      Marker(
        position = latLong,
        title = place.name,
        snippet = buildSnippet(place)
      )
    }
  }

  val bounds = boundsBuilder.build()
  return CameraPosition.fromLatLngZoom(
    bounds.center, getZoomLevel(bounds)
  )
}

private fun placeToLatLng(place: Place): LatLng? {
  val lat = place.latLong?.latitude
  val long = place.latLong?.longitude

  return if (lat != null && long != null) {
    LatLng(lat, long)
  } else {
    null
  }
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

private fun buildSnippet(place: Place): String {
  val builder = java.lang.StringBuilder()
//  val address = place.address.orEmpty()
//  val commaIndex = address.indexOfFirst { it == ',' }
//
//  if (commaIndex != -1) {
//    builder.append(address.substring(0, commaIndex))
//    builder.append("\n")
//  } else {
//    builder.append(address)
//    builder.append("\n")
//  }
//
  builder.append(place.tags.mapNotNull { it.name }.joinToString(", "))

  place.note?.takeIf { it.isNotEmpty() }?.let {
    builder.append(" | $it")
  }

  return builder.toString()
}
