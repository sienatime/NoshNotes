package com.siendy.noshnotes.data.models

import android.graphics.Bitmap

data class PhotoWithAttribution(
  val photo: Bitmap?,
  val attributionHtml: String?
)
