package com.siendy.noshnotes.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupabaseTag(
  @SerialName("id")
  val id: Int,
  @SerialName("created_at")
  val createdAt: String,
  val name: String,
  val icon: String? = null,
  @SerialName("background_color")
  val backgroundColor: String? = null,
  @SerialName("text_color")
  val textColor: String? = null
) {
  fun toTag(): Tag {
    return Tag(
      uid = id.toString(),
      name = name,
      backgroundColor = backgroundColor,
      textColor = textColor
    )
  }
}
