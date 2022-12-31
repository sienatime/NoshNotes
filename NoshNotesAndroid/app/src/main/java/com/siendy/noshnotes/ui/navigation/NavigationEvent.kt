package com.siendy.noshnotes.ui.navigation

sealed class NavigationEvent {
  data class Place(val place: com.siendy.noshnotes.data.models.Place) : NavigationEvent()
}
