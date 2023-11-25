package com.siendy.noshnotes.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.data.models.UIPlace
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.FullScreenLoading
import com.siendy.noshnotes.ui.components.PlacePhoto
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.place.PlaceName
import com.siendy.noshnotes.ui.previews.PreviewData

@Composable
fun PlacesList(
  mainUiState: MainUiState,
  rootNavController: NavHostController?,
  mainViewModel: MainViewModel
) {
  if (mainUiState.loading) {
    FullScreenLoading()
  } else {
    val places = mainUiState.filteredPlaces
    if (places.isEmpty()) {
      Text(
        stringResource(string.no_places)
      )
    } else {
      LazyColumn {
        items(places) { place ->
          PlaceRow(place, rootNavController, mainViewModel)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceRow(
  place: UIPlace,
  rootNavController: NavHostController? = null,
  mainViewModel: MainViewModel? = null
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

    Column {

      Box(
        modifier = Modifier.fillMaxWidth()
      ) {

        PlacePhoto(
          height = 180.dp,
          attributionEndPadding = 8.dp,
          photoMetadata = place.photoMetadata,
          loadPhoto = mainViewModel?.let {
            it::getPhoto
          } ?: { null }
        )

        RemoveButton(
          Modifier.align(Alignment.TopEnd),
          rootNavController,
          place.uid
        )
      }

      Column(
        modifier = Modifier.padding(
          start = 8.dp,
          end = 8.dp,
          bottom = 16.dp
        )
      ) {
        PlaceName(place.name)
        if (place.note?.isNotEmpty() == true) {
          Text(
            text = place.note,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(top = 8.dp)
          )
        }

        AllTags(
          modifier = Modifier.padding(top = 12.dp),
          allTagsState = AllTagsState.fromPlace(place),
          maxRows = 1
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PlaceRowPreview() {
  PlaceRow(PreviewData.previewPlace)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveButton(
  modifier: Modifier,
  rootNavController: NavHostController?,
  placeId: String?
) {
  Box(
    modifier = Modifier
      .padding(8.dp)
      .size(24.dp)
      .clip(CircleShape)
      .background(MaterialTheme.colorScheme.onPrimary)
      .then(modifier)
      .clickable {
        placeId?.let {
          rootNavController?.navigate(Routes.deletePlace(placeId))
        }
      }
  ) {
    Icon(
      Filled.Close,
      contentDescription = stringResource(id = string.delete_place),
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier
        .size(AssistChipDefaults.IconSize)
        .align(Alignment.Center)
    )
  }
}
