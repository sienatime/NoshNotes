package com.siendy.noshnotes.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

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

fun Int.hexToString() = String.format("#%06X", 0xFFFFFF and this)

fun Color.toHexString(): String {
  return this.toArgb().hexToString()
}

fun Float.darken(): Float = if (this < 0.1f) {
  0f
} else {
  this - 0.1f
}
