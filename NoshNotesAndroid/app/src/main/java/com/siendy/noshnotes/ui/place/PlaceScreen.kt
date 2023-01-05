package com.siendy.noshnotes.ui.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Rating
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.NewTagDialog
import com.siendy.noshnotes.ui.components.RatingBar
import com.siendy.noshnotes.ui.components.TagChip
import com.siendy.noshnotes.ui.components.TagState
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.utils.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen(
  placeId: String? = null,
  placeViewModel: PlaceViewModel = viewModel(),
  rootNavController: NavHostController? = null
) {
  val placeUiState by placeViewModel.uiState.collectAsState()
  placeViewModel.getPlace(placeId)

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(id = R.string.add_place_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primary,
          titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
          IconButton(onClick = { rootNavController?.navigateUp() }) {
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
      PlaceContent(
        padding,
        placeUiState,
        placeViewModel,
        rootNavController
      )
    }
  )
}

@Composable
fun PlaceContent(
  padding: PaddingValues,
  placeUiState: PlaceUiState,
  placeViewModel: PlaceViewModel = viewModel(),
  rootNavController: NavHostController? = null
) {
  val navController = rememberNavController()

  NavHost(
    navController,
    Routes.ADD_NEW_PLACE,
    Modifier.padding(padding),
    route = "place_detail"
  ) {
    composable(Routes.ADD_NEW_PLACE) {
      PlaceDetails(
        placeUiState,
        placeViewModel,
        navController,
        rootNavController
      )
    }
    dialog(Routes.ADD_NEW_TAG) {
      NewTagDialog(navController)
    }
  }
}

@Composable
fun PlaceDetails(
  placeUiState: PlaceUiState,
  placeViewModel: PlaceViewModel = viewModel(),
  navController: NavHostController? = null,
  rootNavController: NavHostController? = null
) {
  Column(
    modifier = Modifier
      .verticalScroll(rememberScrollState())
      .padding(
        start = 16.dp,
        end = 16.dp,
        top = 24.dp,
        bottom = 24.dp
      )
  ) {
    if (placeUiState.place == null) {
      Text(stringResource(R.string.place_error))
    } else {
      val place = placeUiState.place

      PlaceName(place.name)
      place.rating?.rating?.let {
        PlaceRating(it, place.rating.total)
      }

      placeUiState.allTagsState?.let {
        AllTags(
          allTagsState = it,
          additionalTag = {
            TagChip(
              tagState = TagState(
                tag = Tag(
                  name = stringResource(R.string.new_tag_chip_label),
                  icon = "add",
                  backgroundColor = "#E1F1F0",
                  textColor = "#018786"
                ),
                clickable = true
              ),
              onTagSelected = {
                navController?.navigate(Routes.ADD_NEW_TAG)
              }
            )
          },
          onTagSelected = { tagState ->
            placeViewModel.onTagSelected(tagState)
          }
        )

        Button(
          onClick = {
            placeViewModel.addPlace(
              place,
              it.tagStates.filter {
                it.selected
              }.map { tagState ->
                tagState.tag
              }
            )
            rootNavController?.navigateUp()
          },
        ) {
          Text(stringResource(id = R.string.save))
        }
      }
    }
  }
}

@Composable
fun PlaceName(name: String?) {
  Text(
    text = name.orEmpty(),
    style = MaterialTheme.typography.headlineLarge
  )
}

@Composable
fun PlaceRating(rating: Double, total: Int?) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = "$rating",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(
        end = 4.dp
      )
    )
    RatingBar(rating)
    Text(
      text = stringResource(id = R.string.total_reviews, total ?: 0),
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(
        start = 4.dp
      )
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PlaceContentPreview() {
  val placeUiState = PlaceUiState(
    place = Place(
      remoteId = "ChIJDwOJGqu5woAR3tTmF6s8bfE",
      name = "Sonoratown",
      latLong = LatLong(34.0539254, -118.3553033),
      address = "5610 San Vicente Blvd, Los Angeles, CA 90019, USA",
      rating = Rating(total = 76, rating = 4.7),
      priceLevel = 1
    ),
    allTagsState = AllTagsState(
      tagStates = listOf(
        TagState(
          tag = Tag(
            name = "Dinner",
            backgroundColor = "#FFCC80",
            textColor = "#757575",
            icon = "dinner",
          )
        ),
        TagState(
          tag = Tag(
            name = "Lunch",
            backgroundColor = "#FFF59D",
            textColor = "#757575",
            icon = "lunch",
          )
        )
      )
    )
  )

  PlaceContent(padding = PaddingValues(), placeUiState = placeUiState)
}

@Preview
@Composable
fun PlaceScreenPreview() {
  PlaceScreen()
}
