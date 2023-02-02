package com.siendy.noshnotes.ui.navigation

object Routes {
  const val MAIN = "main"
  fun place(placeId: String) = "place/$placeId"
  fun newPlace(remoteId: String) = "place/new?remoteId=$remoteId"
  fun deletePlace(placeId: String) = "place/$placeId/delete"

  const val PLACES_LIST = "places_list"
  const val PLACES_MAP = "places_map"
  const val ADD_NEW_PLACE = "new_place"
  const val ADD_NEW_TAG = "new_tag"

  const val AUTOCOMPLETE_REQUEST_CODE = 91
  const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 92
}
