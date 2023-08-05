package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.siendy.noshnotes.R
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.ui.TagIcon
import com.siendy.noshnotes.ui.theme.Gray
import com.siendy.noshnotes.utils.applySelectedStyle
import com.siendy.noshnotes.utils.fromHex
import com.siendy.noshnotes.utils.orEmpty
import com.siendy.noshnotes.utils.partitionInto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTags(
  modifier: Modifier = Modifier,
  allTagsState: AllTagsState,
  maxRows: Int,
  gapSize: Dp = 8.dp,
  additionalTag: @Composable () -> Unit = {},
  onTagSelected: (TagState) -> Unit = {},
  style: TextStyle = MaterialTheme.typography.titleSmall,
  height: Dp = 32.dp,
  iconSize: Dp = AssistChipDefaults.IconSize
) {

  if (maxRows == 1) {
    Box(modifier) {
      FlowRow(
        horizontalGap = gapSize,
        verticalGap = gapSize,
        alignment = Alignment.Start,
      ) {
        allTagsState.tagStates.forEach { tag ->
          TagChip(
            tagState = tag,
            onTagSelected = onTagSelected,
            style = style,
            height = height,
            iconSize = iconSize
          )
        }
        additionalTag()
      }
    }
  } else {
    val partitioned = allTagsState.tagStates.partitionInto(maxRows)

    Box(modifier) {
      Column(
        modifier = Modifier.horizontalScroll(rememberScrollState())
      ) {
        partitioned.forEach { tagsRow ->
          Row(
            modifier = Modifier
              .padding(bottom = gapSize)
          ) {
            tagsRow.forEach { tag ->
              TagChip(
                modifier = Modifier.padding(end = gapSize),
                tag,
                onTagSelected,
                style,
                height,
                iconSize
              )
            }
          }
        }
        additionalTag()
      }
    }
  }
}

private const val MAX_TAG_ROWS = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagChip(
  modifier: Modifier = Modifier,
  tagState: TagState,
  onTagSelected: (TagState) -> Unit = {},
  style: TextStyle = MaterialTheme.typography.titleSmall,
  height: Dp = 32.dp,
  iconSize: Dp = AssistChipDefaults.IconSize
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

  Box(modifier = modifier) {
    AssistChip(
      modifier = Modifier
        .padding(0.dp)
        .height(height),
      label = {
        Text(
          text = tag.name.orEmpty(),
          style = style
        )
      },
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
          Modifier.size(iconSize)
        )
      },
      trailingIcon = {
        if (tagState.selected) {
          Icon(
            Filled.Close,
            contentDescription = stringResource(id = string.unselect),
            Modifier.size(iconSize)
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
          .padding(top = 6.dp, start = 7.dp)
          .size(iconSize + 2.dp)
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
      modifier = Modifier.padding(4.dp),
      allTagsState = AllTagsState.fromTags(tags),
      maxRows = 2
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
      ),
      maxRows = 2
    )
  }
}
