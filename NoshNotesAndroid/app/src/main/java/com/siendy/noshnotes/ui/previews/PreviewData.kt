package com.siendy.noshnotes.ui.previews

import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Rating
import com.siendy.noshnotes.data.models.Tag

object PreviewData {
  val previewPlace = Place(
    remoteId = "ChIJDwOJGqu5woAR3tTmF6s8bfE",
    name = "Sonoratown",
    latLong = LatLong(34.0539254, -118.3553033),
    address = "5610 San Vicente Blvd, Los Angeles, CA 90019, USA",
    rating = Rating(total = 76, rating = 4.7),
    note = "My favorite place!!!",
    priceLevel = 1,
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
    ),
    photoAttributionHtml = "<a href=''>Some Bloke</a>"
  )
}
