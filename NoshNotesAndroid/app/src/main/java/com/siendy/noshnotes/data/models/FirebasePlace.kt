package com.siendy.noshnotes.data.models

import com.google.firebase.database.Exclude

data class FirebasePlace(
  val remoteId: String? = null,
  val note: String? = null,
  val tags: List<String> = emptyList()
) {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return hashMapOf(
      "remoteId" to remoteId,
      "note" to note,
      "tags" to tags.associateWith { true }
    )
  }

  companion object {
    fun fromPlace(place: Place): FirebasePlace {
      return FirebasePlace(
        remoteId = place.remoteId,
        note = place.note,
        tags = place.tags.mapNotNull { it.uid }
      )
    }
  }
}
