package com.siendy.noshnotes.ui.components

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
}
