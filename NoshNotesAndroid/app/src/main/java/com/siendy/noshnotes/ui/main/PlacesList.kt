package com.siendy.noshnotes.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.place.PlaceName
import com.siendy.noshnotes.ui.place.PlaceRating

@Composable
fun PlacesList(places: List<Place>) {
  if (places.isEmpty()) {
    Text(
      stringResource(string.no_places)
    )
  } else {
    LazyColumn() {
      items(places) { place ->
        PlaceRow(place)
      }
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
