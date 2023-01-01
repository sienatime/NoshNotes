package com.siendy.noshnotes.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siendy.noshnotes.TagsRepository
import com.siendy.noshnotes.data.models.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaceViewModel(
  private val tagsRepository: TagsRepository = TagsRepository()
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
                currentUiState.copy(tags = tags)
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

  fun setPlace(place: Place?) {
    _uiState.update { currentUiState ->
      currentUiState.copy(place = place)
    }
  }
}
