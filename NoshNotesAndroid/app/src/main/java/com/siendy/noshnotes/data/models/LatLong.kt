package com.siendy.noshnotes.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatLong(
  val latitude: Double? = null,
  val longitude: Double? = null
) : Parcelable
