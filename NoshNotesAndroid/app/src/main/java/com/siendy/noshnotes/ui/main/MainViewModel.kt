package com.siendy.noshnotes.ui.main

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.widget.Autocomplete
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.repositories.TagsRepository
import com.siendy.noshnotes.domain.ConvertPlaceUseCase
import com.siendy.noshnotes.domain.OpenPlacesAutocompleteUseCase
import com.siendy.noshnotes.ui.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
  private val openPlacesAutocompleteUseCase: OpenPlacesAutocompleteUseCase = OpenPlacesAutocompleteUseCase(),
  private val convertPlaceUseCase: ConvertPlaceUseCase = ConvertPlaceUseCase(),
  private val tagsRepository: TagsRepository = TagsRepository()
) : ViewModel() {

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState: StateFlow<MainUiState> = _uiState

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

  fun writeTag(tag: Tag) {
    tagsRepository.addTag(tag)
  }

  fun openPlacesAutocomplete(activity: Activity) {
    openPlacesAutocompleteUseCase(activity)
  }

  fun getPlaceFromAutocomplete(intent: Intent) {
    val googlePlace = Autocomplete.getPlaceFromIntent(intent)
    val place = convertPlaceUseCase(googlePlace)
    _uiState.update { currentUiState ->
      currentUiState.copy(navigationEvent = NavigationEvent.Place(place))
    }
  }
}

data class MainUiState(
  val places: List<Place> = listOf(),
  val tags: List<Tag> = listOf(),
  val navigationEvent: NavigationEvent? = null
)
