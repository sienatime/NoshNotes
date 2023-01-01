package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.utils.fromHex
import com.siendy.noshnotes.utils.orEmpty

@Composable
fun AllTags(
  tags: List<Tag>,
  modifier: Modifier = Modifier
) {
  Row(modifier = modifier) {
    tags.forEach { tag ->
      TagChip(
        tag,
        modifier = Modifier.padding(end = 8.dp)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagChip(
  tag: Tag,
  modifier: Modifier = Modifier
) {

  val contentColor: Color = tag.textColor?.let {
    Color.fromHex(it)
  } ?: MaterialTheme.colorScheme.onTertiary

  val backgroundColor: Color = tag.backgroundColor?.let {
    Color.fromHex(it)
  } ?: MaterialTheme.colorScheme.tertiary

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
    onClick = { /*TODO*/ }
  )
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

  AllTags(tags = tags)
}
