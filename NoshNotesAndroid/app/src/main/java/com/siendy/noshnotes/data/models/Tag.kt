package com.siendy.noshnotes.data.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
  var uid: String? = null,
  val name: String? = null,
  val icon: String? = null,
  val backgroundColor: String? = null,
  val textColor: String? = null,
) : Parcelable {
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
