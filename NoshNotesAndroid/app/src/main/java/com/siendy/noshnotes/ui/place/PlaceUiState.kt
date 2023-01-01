package com.siendy.noshnotes.ui.place

import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag

data class PlaceUiState(
  val place: Place? = null,
  val tags: List<Tag> = emptyList()
)
