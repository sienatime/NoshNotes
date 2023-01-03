package com.siendy.noshnotes.ui.main

import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.navigation.NavigationEvent

data class MainUiState(
  val filteredPlaces: List<Place> = listOf(),
  val allTagsState: AllTagsState? = null,
  val navigationEvent: NavigationEvent? = null
)
