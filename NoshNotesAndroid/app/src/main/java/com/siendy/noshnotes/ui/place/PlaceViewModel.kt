package com.siendy.noshnotes.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.repositories.PlacesRepository
import com.siendy.noshnotes.data.repositories.TagsRepository
import com.siendy.noshnotes.ui.components.AllTagsState
import com.siendy.noshnotes.ui.components.TagState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaceViewModel(
  private val tagsRepository: TagsRepository = TagsRepository(),
  private val placesRepository: PlacesRepository = PlacesRepository()
) : ViewModel() {
  private val _uiState = MutableStateFlow(PlaceUiState())
  val uiState: StateFlow<PlaceUiState> = _uiState

  init {
    viewModelScope.launch {
      tagsRepository.getTags().collect {
        when {
          it.isSuccess -> {
            it.getOrNull()?.let { tags ->
              _uiState.update { currentUiState ->
                currentUiState.copy(
                  allTagsState = AllTagsState(
                    tagStates = tags.map { tag ->
                      TagState(
                        tag,
                        selected = false,
                        clickable = true
                      )
                    }
                  )
                )
              }
            }
          }
          it.isFailure -> {
            it.exceptionOrNull()?.printStackTrace()
          }
        }
      }
    }
  }

  fun getPlace(placeId: String?) {
    viewModelScope.launch {
      val place = placesRepository.getPlaceByRemoteId(placeId)
      setPlace(place)
    }
  }

  fun setPlace(place: Place?) {
    _uiState.update { currentUiState ->
      currentUiState.copy(place = place)
    }
  }

  fun addPlace(
    place: Place,
    tags: List<Tag>,
    note: String
  ) {
    val updatedPlace = place.copy(
      tags = tags,
      note = note
    )
    placesRepository.addPlace(updatedPlace)
  }

  fun onTagSelected(tagState: TagState) {
    _uiState.update { currentUiState ->
      currentUiState.copy(
        allTagsState = currentUiState.allTagsState?.updateSelectedTag(tagState)
      )
    }
  }
}
