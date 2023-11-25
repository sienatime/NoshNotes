package com.siendy.noshnotes.ui.previews

import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.models.UIPlace

object PreviewData {
  val previewPlace = UIPlace(
    remoteId = "ChIJDwOJGqu5woAR3tTmF6s8bfE",
    name = "Sonoratown",
    latLong = LatLong(34.0539254, -118.3553033),
    address = "5610 San Vicente Blvd, Los Angeles, CA 90019, USA",
    note = "My favorite place!!!",
    tags = listOf(
      Tag(
        name = "Lunch"
      ),
      Tag(
        name = "Dinner"
      ),
      Tag(
        name = "Tacos"
      ),
      Tag(
        name = "Mexican"
      )
    )
  )
}
