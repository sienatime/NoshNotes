package com.siendy.noshnotes.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
  val remoteId: String?,
  val name: String?,
  val latLong: LatLong,
  val address: String?,
  val rating: Rating,
  val priceLevel: PriceLevel?
) : Parcelable {
  // lazy loaded image
}
