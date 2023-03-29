package com.siendy.noshnotes.ui.components

import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag

data class AllTagsState(
  val tagStates: List<TagState>
) {

  private val tagStateMap: Map<String, TagState> = tagStates.mapNotNull { tagState ->
    tagState.tag.uid?.let { uid ->
      uid to tagState
    }
  }.toMap()

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

  fun getStateForTagUid(uid: String?): TagState? {
    return tagStateMap[uid]
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
