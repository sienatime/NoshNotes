package com.siendy.noshnotes.data.models

data class FirebasePlace(
  val uid: String? = null,
  val remoteId: String? = null,
  val note: String? = null,
  val tags: Map<String, Boolean> = mapOf()
) {
  fun toMap(): Map<String, Any?> {
    return hashMapOf(
      "uid" to uid,
      "remoteId" to remoteId,
      "note" to note,
      "tags" to tags
    )
  }

  fun tagIds(): Set<String> = tags.keys

  fun hasAllTags(tagsSet: Set<String>): Boolean {
    return tagIds().containsAll(tagsSet)
  }

  fun hasTag(tagId: String): Boolean {
    return tagIds().contains(tagId)
  }

  companion object {
    fun fromPlace(place: Place): FirebasePlace {
      return FirebasePlace(
        uid = place.uid,
        remoteId = place.remoteId,
        note = place.note,
        tags = place.tags.filter { it.uid != null }.associate { it.uid!! to true }
      )
    }
  }
}
