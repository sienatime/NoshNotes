package com.siendy.noshnotes.ui.place

import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.components.AllTagsState

data class PlaceUiState(
  val place: Place? = null,
  val originalTags: List<String> = emptyList(),
  val allTagsState: AllTagsState? = null,
  val loading: Boolean = true
)
