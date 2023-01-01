package com.siendy.noshnotes.data.models

import com.google.firebase.database.Exclude

data class Tag(
  val uid: String? = null,
  val name: String? = null,
  val icon: String? = null,
  val backgroundColor: String? = null,
  val textColor: String? = null,
) {
  @Exclude
  fun toMap(): Map<String, Any?> {
    return hashMapOf(
      "name" to name,
      "icon" to icon,
      "backgroundColor" to backgroundColor,
      "textColor" to textColor
    )
  }
}
