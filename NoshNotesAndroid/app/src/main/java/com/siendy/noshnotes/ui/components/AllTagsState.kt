package com.siendy.noshnotes.ui.components

import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag

data class AllTagsState(
  val tagStates: List<TagState>
) {
  fun updateSelectedTag(tagState: TagState): AllTagsState {
    return tagStates.indexOfFirst { it == tagState }.let { index ->
      if (index == -1) {
        this
      } else {
        val tagStateToUpdate = tagStates[index]
        val updatedTagState = tagStateToUpdate.copy(
          selected = !tagStateToUpdate.selected
        )
        val updatedStates: List<TagState> = tagStates.toMutableList().apply {
          this[index] = updatedTagState
        }
        AllTagsState(
          tagStates = updatedStates
        )
      }
    }
  }

  companion object {
    fun fromPlace(place: Place): AllTagsState {
      return fromTags(place.tags)
    }

    fun fromTags(
      tags: List<Tag>,
      selected: Boolean = false,
      clickable: Boolean = false
    ): AllTagsState {
      return AllTagsState(
        tags.map { tag ->
          TagState(
            tag,
            selected = selected,
            clickable = clickable
          )
        }
      )
    }
  }
}
