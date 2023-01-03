package com.siendy.noshnotes.data.models

import com.google.firebase.database.Exclude
import com.siendy.noshnotes.utils.containsAny

data class FirebasePlace(
  var uid: String? = null,
  val remoteId: String? = null,
  val note: String? = null,
  val tags: Map<String, Boolean> = mapOf()
) {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return hashMapOf(
      "remoteId" to remoteId,
      "note" to note,
      "tags" to tags
    )
  }

  @Exclude
  fun hasAnyTag(tagsSet: Set<String>): Boolean {
    return tags.keys.containsAny(tagsSet)
  }

  companion object {
    fun fromPlace(place: Place): FirebasePlace {
      return FirebasePlace(
        remoteId = place.remoteId,
        note = place.note,
        tags = place.tags.filter { it.uid != null }.associate { it.uid!! to true }
      )
    }
  }
}
