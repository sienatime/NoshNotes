package com.siendy.noshnotes.utils

import androidx.compose.ui.graphics.Color

fun Color.Companion.fromHex(hexColor: String) = Color(android.graphics.Color.parseColor(hexColor))
