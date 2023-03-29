package com.siendy.noshnotes.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.siendy.noshnotes.data.models.PhotoWithAttribution
import com.siendy.noshnotes.data.repositories.PlacesRepository
import com.siendy.noshnotes.data.repositories.TagsRepository
import com.siendy.noshnotes.domain.OpenPlacesAutocompleteUseCase
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.TagState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val openPlacesAutocompleteUseCase: OpenPlacesAutocompleteUseCase,
  private val tagsRepository: TagsRepository,
  private val placesRepository: PlacesRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState: StateFlow<MainUiState> = _uiState

  init {
    viewModelScope.launch {
      tagsRepository.getTags().collect { tags ->
        // this is weird but can't figure out how to do it internal to tagsrepo
        // cache needs to be initialized before the flow returns
        tagsRepository.initCache(tags)

        placesRepository.getPlacesByTagIds(emptyList()).collect { filteredPlaces ->
          _uiState.update { currentUiState ->
            val oldTagStates = currentUiState.allTagsState?.tagStates.orEmpty()
            currentUiState.copy(
              filteredPlaces = filteredPlaces,
              allTagsState = AllTagsState(
                tags.map { tag ->
                  TagState(
                    tag,
                    selected = oldTagStates.firstOrNull { tagState ->
                      tagState.tag.uid == tag.uid
                    }?.selected ?: false,
                    clickable = true
                  )
                }
              ),
              loading = false
            )
          }
        }
      }
    }
  }

  fun openPlacesAutocomplete(activity: Activity) {
    openPlacesAutocompleteUseCase(activity)
  }

  fun onTagSelected(tagState: TagState) {
    _uiState.update { currentUiState ->
      currentUiState.copy(
        allTagsState = currentUiState.allTagsState?.updateSelectedTag(tagState),
        loading = true
      )
    }
    getPlacesBySelectedTags()
  }

  fun deletePlace(placeId: String?) {
    if (placeId == null) return
    _uiState.update { currentUiState ->
      currentUiState.copy(
        loading = true
      )
    }
    viewModelScope.launch {
      placesRepository.deletePlace(placeId)
      _uiState.update { currentUiState ->
        currentUiState.copy(
          loading = false
        )
      }
    }
  }

  fun onLocationPermissionGranted(latLng: LatLng) {
    _uiState.update { currentUiState ->
      currentUiState.copy(
        locationPermissionGranted = true,
        defaultMapLocation = latLng
      )
    }
  }

  suspend fun getPhoto(photoMetadata: PhotoMetadata): PhotoWithAttribution? {
    return placesRepository.getPhoto(photoMetadata)
  }

  private var filterJob: Job? = null

  private fun getPlacesBySelectedTags() {
    val selectedTagIds: List<String> = _uiState.value.allTagsState?.tagStates?.filter {
      it.selected
    }?.mapNotNull {
      it.tag.uid
    }.orEmpty()

    filterJob?.cancel()

    filterJob = viewModelScope.launch {
      placesRepository.getPlacesByTagIds(selectedTagIds).collect { filteredPlaces ->
        _uiState.update { currentUiState ->
          currentUiState.copy(
            filteredPlaces = filteredPlaces,
            loading = false
          )
        }
      }
    }
  }
}
