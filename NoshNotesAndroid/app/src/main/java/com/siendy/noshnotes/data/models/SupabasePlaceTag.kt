package com.siendy.noshnotes.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupabasePlaceTag(
  @SerialName("place_id")
  val placeId: Int,
  @SerialName("tag_id")
  val tagId: Int
)
