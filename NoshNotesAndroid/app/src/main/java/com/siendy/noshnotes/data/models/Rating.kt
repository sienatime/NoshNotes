package com.siendy.noshnotes.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
  val total: Int?,
  val rating: Double?
) : Parcelable
