package com.siendy.noshnotes.data.models

import io.ktor.resources.Resource

@Resource("/places")
class KtorPlaces(
  val select: String = "*",
  val id: String? = null
)

@Resource("/tags")
class KtorTags(val select: String = "*",)

@Resource("/place_tags")
class KtorPlaceTags(
  val select: String = "*",
  val tag_id: String? = null,
  val place_id: String? = null
)
