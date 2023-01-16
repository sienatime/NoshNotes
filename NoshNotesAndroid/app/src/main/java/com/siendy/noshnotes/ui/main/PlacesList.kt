package com.siendy.noshnotes.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.TagState
import com.siendy.noshnotes.ui.place.PlaceName
import com.siendy.noshnotes.ui.place.PlaceRating
import com.siendy.noshnotes.utils.orEmpty

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

      Text(
        text = place.note.orEmpty(),
        fontStyle = FontStyle.Italic,
        modifier = Modifier.padding(top = 8.dp)
      )

      AllTags(
        allTagsState = AllTagsState(
          place.tags.map { tag ->
            TagState(tag)
          }
        )
      )
    }
  }
}
