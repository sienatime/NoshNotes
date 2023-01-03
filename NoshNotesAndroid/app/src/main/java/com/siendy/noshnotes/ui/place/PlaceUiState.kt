package com.siendy.noshnotes.ui.place

import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.navigation.NavigationEvent

data class PlaceUiState(
  val place: Place? = null,
  val allTagsState: AllTagsState? = null,
  val navigationEvent: NavigationEvent? = null
)
