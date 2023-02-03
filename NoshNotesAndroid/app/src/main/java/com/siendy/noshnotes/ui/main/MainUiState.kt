package com.siendy.noshnotes.ui.main

import com.google.android.gms.maps.model.LatLng
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.UIConstants.DEFAULT_LOCATION
import com.siendy.noshnotes.ui.components.AllTagsState

data class MainUiState(
  val filteredPlaces: List<Place> = listOf(),
  val allTagsState: AllTagsState? = null,
  val loading: Boolean = true,
  val locationPermissionGranted: Boolean = false,
  val defaultMapLocation: LatLng = DEFAULT_LOCATION
)
