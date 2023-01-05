package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.ui.theme.Gray
import com.siendy.noshnotes.utils.applySelectedStyle
import com.siendy.noshnotes.utils.fromHex
import com.siendy.noshnotes.utils.orEmpty

@Composable
fun AllTags(
  allTagsState: AllTagsState,
  additionalTag: @Composable () -> Unit = {},
  onTagSelected: (TagState) -> Unit = {}
) {
  FlowRow(
    horizontalGap = 8.dp,
    verticalGap = 0.dp,
    alignment = Alignment.Start,
  ) {
    allTagsState.tagStates.forEach { tag ->
      TagChip(
        tag,
        onTagSelected = onTagSelected
      )
    }
    additionalTag()
  }
}

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
    Color.DarkGray
  ).applySelectedStyle(tagState.selected)

  val backgroundColor: Color = getColor(
    tag.backgroundColor,
    Gray
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
    "add" -> R.drawable.ic_baseline_add_circle_outline_24
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
    ),
    Tag(
      name = "Brunch"
    ),
    Tag(
      name = "Sushi"
    ),
    Tag(
      name = "Bars"
    )
  )

  Column(Modifier.width(320.dp)) {
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
