package com.siendy.noshnotes.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupabasePlace(
  @SerialName("id")
  val id: Int,
  @SerialName("created_at")
  val createdAt: String,
  @SerialName("remote_id")
  val remoteId: String,
  val note: String? = null,
  @SerialName("place_tags")
  val placeTags: List<SupabasePlaceTag> = emptyList()
) {
  fun toDBPlace(tagIds: List<String>? = null): DBPlace {
    return DBPlace(
      uid = id.toString(),
      remoteId = remoteId,
      note = note,
      tagIds = tagIds ?: placeTags.map {
        it.tagId.toString()
      }
    )
  }
}
