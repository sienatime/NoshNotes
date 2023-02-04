package com.siendy.noshnotes.ui

import androidx.annotation.DrawableRes
import com.siendy.noshnotes.R

enum class TagIcon(
  val iconName: String,
  @DrawableRes val drawableId: Int
) {
  DEFAULT(
    "pin",
    R.drawable.ic_baseline_location_on_24
  ),
  DINNER(
    "dinner",
    R.drawable.ic_baseline_dinner_dining_24
  ),
  LUNCH(
    "lunch",
    R.drawable.ic_baseline_lunch_dining_24
  ),
  BREAKFAST(
    "breakfast",
    R.drawable.ic_baseline_breakfast_dining_24
  ),
  BAR(
    "bar",
    R.drawable.ic_baseline_local_bar_24
  ),
  COFFEE(
    "coffee",
    R.drawable.ic_baseline_coffee_24
  ),
  RESTAURANT(
    "restaurant",
    R.drawable.ic_baseline_restaurant_24
  ),
  DESSERT(
    "dessert",
    R.drawable.ic_baseline_icecream_24
  ),
  HEART(
    "heart",
    R.drawable.ic_baseline_heart_24
  );

  companion object {
    fun drawableForName(name: String?): Int {
      return values().firstOrNull {
        it.iconName == name
      }?.drawableId ?: DEFAULT.drawableId
    }
  }
}
