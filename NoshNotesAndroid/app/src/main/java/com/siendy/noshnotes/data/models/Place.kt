package com.siendy.noshnotes.data.models

data class Place(
  val id: String,
  val remoteId: String,
  val name: String,
  val latLong: LatLong,
  val address: String,
  val rating: Rating,
  val priceLevel: PriceLevel
) {
  // lazy loaded image
}
