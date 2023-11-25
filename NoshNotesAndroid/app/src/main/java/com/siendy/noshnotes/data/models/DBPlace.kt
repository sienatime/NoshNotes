package com.siendy.noshnotes.data.models

data class DBPlace(
  val uid: String? = null,
  val remoteId: String? = null,
  val note: String? = null,
  val tagIds: List<String> = emptyList()
) {
  fun hasAllTags(tagsSet: Set<String>): Boolean {
    return tagIds.containsAll(tagsSet)
  }
}
