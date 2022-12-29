package com.siendy.noshnotes.data.models

data class PlaceWithTags(
  val place: Place,
  val createdAt: Long,
  val note: String?,
  val tags: List<Tag>
)
