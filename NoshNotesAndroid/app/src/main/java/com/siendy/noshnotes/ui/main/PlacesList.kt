package com.siendy.noshnotes.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Rating
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.TagState
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.place.PlaceName
import com.siendy.noshnotes.ui.place.PlaceRating

@Composable
fun PlacesList(places: List<Place>, rootNavController: NavHostController?) {
  if (places.isEmpty()) {
    Text(
      stringResource(string.no_places)
    )
  } else {
    LazyColumn() {
      items(places) { place ->
        PlaceRow(place, rootNavController)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceRow(
  place: Place,
  rootNavController: NavHostController? = null
) {
  Card(
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 4.dp
    ),
    modifier = Modifier.padding(vertical = 8.dp),
    onClick = {
      place.uid?.let { placeId ->
        rootNavController?.navigate(Routes.place(placeId))
      }
    }
  ) {

    Column(
      modifier = Modifier.padding(8.dp)
    ) {
      PlaceName(place.name)
      place.rating?.rating?.let {
        PlaceRating(it, place.rating.total)

        if (place.note?.isNotEmpty() == true) {
          Text(
            text = place.note,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 8.dp)
          )
        }

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
}

@Preview(showBackground = true)
@Composable
fun PlaceRowPreview() {
  val place = Place(
    remoteId = "ChIJDwOJGqu5woAR3tTmF6s8bfE",
    name = "Sonoratown",
    latLong = LatLong(34.0539254, -118.3553033),
    address = "5610 San Vicente Blvd, Los Angeles, CA 90019, USA",
    rating = Rating(total = 76, rating = 4.7),
    note = "My favorite place!!!",
    priceLevel = 1
  )
  PlaceRow(place)
}
