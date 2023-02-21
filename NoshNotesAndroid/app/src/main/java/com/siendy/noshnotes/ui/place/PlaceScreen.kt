package com.siendy.noshnotes.ui.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.NewTagDialog
import com.siendy.noshnotes.ui.components.PlacePhoto
import com.siendy.noshnotes.ui.components.RatingBar
import com.siendy.noshnotes.ui.components.TagChip
import com.siendy.noshnotes.ui.components.TagState
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.previews.PreviewData
import com.siendy.noshnotes.utils.orEmpty

@Composable
fun PlaceScreen(
  placeRemoteId: String? = null,
  placeId: String? = null,
  placeViewModel: PlaceViewModel = hiltViewModel(),
  rootNavController: NavHostController? = null
) {
  when {
    placeRemoteId != null -> {
      placeViewModel.getPlaceByRemoteId(placeRemoteId)

      PlaceAddOrEdit(
        title = stringResource(id = R.string.add_place_title),
        placeViewModel,
        rootNavController
      )
    }
    placeId != null -> {
      placeViewModel.getPlaceById(placeId)

      PlaceAddOrEdit(
        title = stringResource(id = R.string.update_place_title),
        placeViewModel,
        rootNavController
      )
    }
    else -> placeViewModel.failed()
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceAddOrEdit(
  title: String,
  placeViewModel: PlaceViewModel = hiltViewModel(),
  rootNavController: NavHostController? = null
) {
  val placeUiState by placeViewModel.uiState.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(title) },
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
  placeViewModel: PlaceViewModel = hiltViewModel(),
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
      NewTagDialog(
        navController,
        placeViewModel
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetails(
  placeUiState: PlaceUiState,
  placeViewModel: PlaceViewModel? = null,
  navController: NavHostController? = null,
  rootNavController: NavHostController? = null
) {
  Column(
    modifier = Modifier
      .verticalScroll(rememberScrollState())
      .fillMaxWidth()
      .padding(
        start = 16.dp,
        end = 16.dp,
        top = 24.dp,
        bottom = 24.dp
      )
  ) {
    if (placeUiState.place == null) {
      if (placeUiState.loading) {
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.CenterHorizontally)
        )
      } else {
        Text(stringResource(R.string.place_error))
      }
    } else {
      val place = placeUiState.place

      PlacePhoto(
        height = 180.dp,
        photoMetadata = place.photoMetadata,
        loadPhoto = placeViewModel?.let {
          it::getPhoto
        } ?: { null }
      )

      PlaceName(place.name)
      place.rating?.rating?.let {
        PlaceRating(it, place.rating.total)
      }

      val noteValue = remember {
        mutableStateOf(
          TextFieldValue(place.note.orEmpty())
        )
      }

      TextField(
        value = noteValue.value,
        onValueChange = { noteValue.value = it },
        placeholder = { Text(text = stringResource(id = R.string.note_placeholder)) },
        modifier = Modifier.padding(top = 28.dp, bottom = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
          capitalization = KeyboardCapitalization.Sentences
        )
      )

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
              },
            )
          },
          onTagSelected = { tagState ->
            placeViewModel?.onTagSelected(tagState)
          },
        )

        Button(
          onClick = {
            placeViewModel?.addPlace(
              place,
              it.tagStates.filter {
                it.selected
              }.map { tagState ->
                tagState.tag
              },
              noteValue.value.text.trim(),
              placeUiState.originalTags
            )
            rootNavController?.navigateUp()
          },
          modifier = Modifier.align(Alignment.End)
        ) {
          Text(stringResource(id = R.string.save).uppercase())
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
fun PlaceLoadingPreview() {
  val placeUiState = PlaceUiState(loading = true)
  PlaceDetails(placeUiState = placeUiState)
}

@Preview(showBackground = true)
@Composable
fun PlaceContentPreview() {
  val placeUiState = PlaceUiState(
    place = PreviewData.previewPlace,
    allTagsState = AllTagsState(
      tagStates = listOf(
        TagState(
          tag = Tag(
            name = "Dinner",
            backgroundColor = "#FFCC80",
            textColor = "#444444",
            icon = "dinner",
          )
        ),
        TagState(
          tag = Tag(
            name = "Lunch",
            backgroundColor = "#FFF59D",
            textColor = "#444444",
            icon = "lunch",
          )
        )
      )
    )
  )

  PlaceDetails(placeUiState = placeUiState)
}
