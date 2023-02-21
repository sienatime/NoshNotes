package com.siendy.noshnotes.data.models

import com.google.android.libraries.places.api.model.PhotoMetadata

data class Place(
  var uid: String? = null,
  val remoteId: String? = null,
  val name: String? = null,
  val latLong: LatLong? = null,
  val address: String? = null,
  val rating: Rating? = null,
  val priceLevel: Int? = null,
  val note: String? = null,
  val tags: List<Tag> = emptyList(),
  val photoMetadata: PhotoMetadata? = null
)
