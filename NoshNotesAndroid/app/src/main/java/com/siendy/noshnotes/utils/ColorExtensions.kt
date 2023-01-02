package com.siendy.noshnotes.utils

import androidx.compose.ui.graphics.Color

fun Color.Companion.fromHex(hexColor: String) = Color(android.graphics.Color.parseColor(hexColor))

fun Color.applySelectedStyle(selected: Boolean): Color {
  return if (selected) {
    Color(
      red = this.red.darken(),
      green = this.green.darken(),
      blue = this.blue.darken()
    )
  } else {
    this
  }
}

fun Float.darken(): Float = if (this < 0.25f) {
  0f
} else {
  this - 0.25f
}
