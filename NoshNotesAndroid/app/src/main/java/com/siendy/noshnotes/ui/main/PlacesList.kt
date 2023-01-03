package com.siendy.noshnotes.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.place.PlaceName
import com.siendy.noshnotes.ui.place.PlaceRating

@Composable
fun PlacesList(places: List<Place>) {
  LazyColumn {
    items(places) { place ->
      PlaceRow(place)
    }
  }
}

@Composable
fun PlaceRow(place: Place) {
  Column {
    PlaceName(place.name)
    place.rating?.rating?.let {
      PlaceRating(it, place.rating.total)
    }
  }
}
