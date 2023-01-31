package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Filled
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
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.ui.TagIcon
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

  Box {
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
          painterResource(TagIcon.drawableForName(tag.icon)),
          contentDescription = null,
          Modifier.size(AssistChipDefaults.IconSize)
        )
      },
      trailingIcon = {
        if (tagState.selected) {
          Icon(
            Filled.Close,
            contentDescription = stringResource(id = string.unselect),
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
    if (tagState.selected) {
      Image(
        painterResource(id = R.drawable.ic_selected_circle),
        contentDescription = null,
        Modifier
          .padding(top = 14.dp, start = 7.dp)
          .size(AssistChipDefaults.IconSize + 2.dp)
      )
    }
  }
}

private fun getColor(
  hex: String?,
  default: Color
): Color {
  return hex?.let {
    Color.fromHex(it)
  } ?: default
}

@Preview(showBackground = true)
@Composable
fun TagChipPreview() {

  val tags = listOf(
    Tag(
      name = "Dinner",
      backgroundColor = "#FFCC80",
      icon = "dinner",
    ),
    Tag(
      name = "Lunch",
      backgroundColor = "#FFF59D",
      icon = "lunch"
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
