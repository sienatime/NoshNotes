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

  fun newState() {
    _uiState.update {
      PlaceUiState()
    }
  }

  fun getPlaceByRemoteId(placeRemoteId: String?) {
    viewModelScope.launch {
      val place = placesRepository.getPlaceByRemoteId(placeRemoteId)
      tagsRepository.getTags().collect { tags ->
        _uiState.update { currentUiState ->
          currentUiState.copy(
            place = place,
            originalTags = emptyList(),
            allTagsState = AllTagsState(
              tagStates = tags.map { tag ->
                TagState(
                  tag,
                  selected = false,
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

  fun getPlaceById(placeId: String?) {
    viewModelScope.launch {
      tagsRepository.getTags().collect { tags ->
        val place = placesRepository.getPlaceById(placeId, allTagsMap(tags))

        val placeTagIds = place?.tags?.map { it.uid }.orEmpty()

        _uiState.update { currentUiState ->
          currentUiState.copy(
            place = place,
            originalTags = place?.tags?.mapNotNull { it.uid }.orEmpty(),
            allTagsState = AllTagsState(
              tagStates = tags.map { tag ->
                TagState(
                  tag,
                  selected = placeTagIds.contains(tag.uid),
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

  fun failed() {
    _uiState.update { currentUiState ->
      currentUiState.copy(loading = false)
    }
  }

  fun addPlace(
    place: Place,
    newTags: List<Tag>,
    note: String,
    originalTags: List<String>
  ) {
    val updatedPlace = place.copy(
      tags = newTags,
      note = note
    )
    placesRepository.updatePlace(updatedPlace, originalTags)
  }

  fun onTagSelected(tagState: TagState) {
    _uiState.update { currentUiState ->
      currentUiState.copy(
        allTagsState = currentUiState.allTagsState?.updateSelectedTag(tagState)
      )
    }
  }

  private fun allTagsMap(tags: List<Tag>): Map<String, Tag> {
    return tags.mapNotNull {
      it.uid?.let { uid ->
        uid to it
      }
    }.toMap()
  }
}
