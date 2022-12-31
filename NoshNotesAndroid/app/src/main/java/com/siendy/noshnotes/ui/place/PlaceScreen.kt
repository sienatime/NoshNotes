package com.siendy.noshnotes.ui.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.PriceLevel
import com.siendy.noshnotes.data.models.Rating
import com.siendy.noshnotes.ui.components.RatingBar
import com.siendy.noshnotes.ui.theme.NoshNotesTheme
import com.siendy.noshnotes.utils.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen(place: Place) {
  val navController = rememberNavController()

  NoshNotesTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(stringResource(id = R.string.add_place_title)) },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
          ),
          navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
              Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colorScheme.onPrimary
              )
            }
          }
        )
      },
      content = { padding ->
        Column(
          modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(
              start = 16.dp,
              end = 16.dp,
              top = 24.dp + padding.calculateTopPadding(),
              bottom = 24.dp + padding.calculateBottomPadding()
            )
        ) {
          Text(
            text = place.name.orEmpty(),
            style = MaterialTheme.typography.headlineLarge
          )
          place.rating.rating?.let {
            Row(
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = "${place.rating.rating}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                  end = 4.dp
                )
              )
              RatingBar(place.rating.rating)
              Text(
                text = stringResource(id = R.string.total_reviews, place.rating.total ?: 0),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                  start = 4.dp
                )
              )
            }
          }
        }
      }
    )
  }
}

@Preview
@Composable
fun PlaceScreenPreview() {

  PlaceScreen(
    place = Place(
      remoteId = "ChIJDwOJGqu5woAR3tTmF6s8bfE",
      name = "Sonoratown",
      latLong = LatLong(34.0539254, -118.3553033),
      address = "5610 San Vicente Blvd, Los Angeles, CA 90019, USA",
      rating = Rating(total = 76, rating = 4.7),
      priceLevel = PriceLevel.ONE
    )
  )
}
