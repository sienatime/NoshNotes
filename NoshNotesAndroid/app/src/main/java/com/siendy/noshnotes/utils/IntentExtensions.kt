package com.siendy.noshnotes.utils

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? {
  return if (SDK_INT >= 33) {
    getParcelableExtra(key, T::class.java)
  } else {
    @Suppress("DEPRECATION") getParcelableExtra(key) as? T
  }
}
