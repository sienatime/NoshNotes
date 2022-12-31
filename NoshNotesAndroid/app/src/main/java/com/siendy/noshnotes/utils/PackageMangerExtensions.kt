package com.siendy.noshnotes.utils

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.os.Build

fun PackageManager.getApplicationInfoCompat(packageName: String, flags: Int = 0): ApplicationInfo {
  return if (Build.VERSION.SDK_INT >= 33) {
    getApplicationInfo(packageName, ApplicationInfoFlags.of(flags.toLong()))
  } else {
    @Suppress("DEPRECATION")
    getApplicationInfo(packageName, flags)
  }
}
