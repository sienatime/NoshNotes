package com.siendy.noshnotes.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.maps.SupportMapFragment
import com.siendy.noshnotes.databinding.MapFragmentBinding

@Composable
fun PlacesMap(
  mainViewModel: MainViewModel
) {
  AndroidViewBinding(MapFragmentBinding::inflate) {
    val mapFragment = this.mapFragment.getFragment<SupportMapFragment>()
    mapFragment.getMapAsync {
      mainViewModel.onMapLoaded(it)
    }
  }
}
