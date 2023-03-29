package com.siendy.noshnotes.ui

import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.siendy.noshnotes.ui.theme.Gray

object UIConstants {
  val tagColors: List<Color> = listOf(
    Gray,
    Color(0xFFF8BBD0),
    Color(0xFFFFCC80),
    Color(0xFFFFF59D),
    Color(0xFFDCEDC8),
    Color(0xFFB2DFDB),
    Color(0xFFB2EBF2),
    Color(0xFFBBDEFB),
    Color(0xFFE1BEE7),
  )

  val DEFAULT_LOCATION = LatLng(34.083444, -118.361784)
  // a float between 2 and 21. bigger numbers are more zoomed in.
  const val DEFAULT_ZOOM = 14f
  const val MAX_ZOOM = 10f
}
