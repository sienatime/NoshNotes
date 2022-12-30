package com.siendy.noshnotes.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.navigation.Routes

sealed class TabDestination(
  val route: String,
  @DrawableRes val iconId: Int,
  @StringRes val titleId: Int
) {
  object PlacesList : TabDestination(
    Routes.PLACES_LIST,
    R.drawable.ic_baseline_list_24,
    R.string.list
  )

  object PlacesMap : TabDestination(
    Routes.PLACES_MAP,
    R.drawable.ic_baseline_location_on_24,
    R.string.map
  )
}
