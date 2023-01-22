package com.siendy.noshnotes.ui

import androidx.compose.ui.graphics.Color
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.theme.Gray

object UIConstants {
  const val MAX_RATING = 5.0
  const val MIN_RATING = 0.0
  const val HALF_RATING_MIN = 0.3
  const val HALF_RATING_MAX = 0.8

  val tagColors: List<Color> = listOf(
    Gray,
    Color(0xFFF8BBD0),
    Color(0xFFFFCC80),
    Color(0xFFFFF59D),
    Color(0xFFDCEDC8),
    Color(0xFFB2DFDB),
    Color(0xFFB2EBF2),
    Color(0xFFBBDEFB),
    Color(0xFFE1BEE7),
  )

  val tagIcons: List<Int> = listOf(
    R.drawable.ic_baseline_breakfast_dining_24,
    R.drawable.ic_baseline_lunch_dining_24,
    R.drawable.ic_baseline_dinner_dining_24,
    R.drawable.ic_baseline_local_bar_24,
    R.drawable.ic_baseline_coffee_24,
    R.drawable.ic_baseline_restaurant_24,
    R.drawable.ic_baseline_icecream_24,
    R.drawable.ic_baseline_heart_24
  )
}
