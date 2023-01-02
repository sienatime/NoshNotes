package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.utils.applySelectedStyle
import com.siendy.noshnotes.utils.fromHex
import com.siendy.noshnotes.utils.orEmpty

@Composable
fun AllTags(
  allTagsState: AllTagsState,
  modifier: Modifier = Modifier,
  onTagSelected: (TagState) -> Unit = {}
) {
  Row(modifier = modifier) {
    allTagsState.tagStates.forEach { tag ->
      TagChip(
        tag,
        modifier = Modifier.padding(end = 8.dp),
        onTagSelected
      )
    }
  }
}

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

data class TagState(
  val tag: Tag,
  val selected: Boolean = false,
  val clickable: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagChip(
  tagState: TagState,
  modifier: Modifier = Modifier,
  onTagSelected: (TagState) -> Unit = {}
) {
  val tag = tagState.tag

  val contentColor: Color = getColor(
    tag.textColor,
    MaterialTheme.colorScheme.onTertiary
  ).applySelectedStyle(tagState.selected)

  val backgroundColor: Color = getColor(
    tag.backgroundColor,
    MaterialTheme.colorScheme.tertiary
  ).applySelectedStyle(tagState.selected)

  backgroundColor.alpha + 0.2f

  AssistChip(
    modifier = modifier,
    label = { Text(tag.name.orEmpty()) },
    colors = AssistChipDefaults.assistChipColors(
      containerColor = backgroundColor,
      labelColor = contentColor,
      leadingIconContentColor = contentColor,
      trailingIconContentColor = contentColor,
    ),
    leadingIcon = {
      Icon(
        painterResource(iconNameToDrawable(tag.icon)),
        contentDescription = null,
        Modifier.size(AssistChipDefaults.IconSize)
      )
    },
    trailingIcon = {
      if (tagState.selected) {
        Icon(
          Icons.Filled.Close,
          contentDescription = stringResource(id = R.string.unselect),
          Modifier.size(AssistChipDefaults.IconSize)
        )
      }
    },
    onClick = {
      if (tagState.clickable) {
        onTagSelected.invoke(tagState)
      }
    }
  )
}

private fun getColor(
  hex: String?,
  default: Color
): Color {
  return hex?.let {
    Color.fromHex(it)
  } ?: default
}

private fun iconNameToDrawable(iconName: String?): Int {
  return when (iconName) {
    "dinner" -> R.drawable.ic_baseline_dinner_dining_24
    "lunch" -> R.drawable.ic_baseline_lunch_dining_24
    else -> R.drawable.ic_baseline_location_on_24
  }
}

@Preview(showBackground = true)
@Composable
fun TagChipPreview() {

  val tags = listOf(
    Tag(
      name = "Dinner",
      backgroundColor = "#FFCC80",
      textColor = "#757575",
      icon = "dinner",
    ),
    Tag(
      name = "Lunch",
      backgroundColor = "#FFF59D",
      textColor = "#757575",
      icon = "lunch",
    )
  )

  Column {
    AllTags(
      allTagsState = AllTagsState(
        tagStates = tags.map { tag ->
          TagState(tag = tag)
        }
      )
    )

    AllTags(
      allTagsState = AllTagsState(
        tagStates = tags.map { tag ->
          TagState(
            tag = tag,
            selected = true,
            clickable = true
          )
        }
      )
    )
  }
}
