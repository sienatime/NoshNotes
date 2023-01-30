package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.repositories.TagsRepository
import com.siendy.noshnotes.ui.TagIcon
import com.siendy.noshnotes.ui.UIConstants
import com.siendy.noshnotes.ui.theme.DarkGray
import com.siendy.noshnotes.ui.theme.Gray
import com.siendy.noshnotes.ui.theme.Purple80
import com.siendy.noshnotes.utils.toHexString

@Preview
@Composable
fun NewTagDialog(
  parentNavController: NavHostController? = null
) {
  val nameValue = remember { mutableStateOf(TextFieldValue()) }
  val selectedColor = remember { mutableStateOf(Gray) }
  val selectedIcon = remember { mutableStateOf(TagIcon.DEFAULT) }

  Dialog(
    onConfirm = {
      TagsRepository().addTag(
        Tag(
          name = nameValue.value.text,
          backgroundColor = selectedColor.value.toHexString(),
          icon = selectedIcon.value.iconName
        )
      )
    },
    parentNavController,
    confirmTextId = R.string.save
  ) {
    TagNameInput(nameValue)

    TagIcons(selectedIcon)
    TagColors(selectedColor)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagNameInput(nameValue: MutableState<TextFieldValue>) {
  TextField(
    value = nameValue.value,
    onValueChange = { nameValue.value = it },
    placeholder = {
      Text(text = stringResource(id = R.string.new_tag_name))
    },
    keyboardOptions = KeyboardOptions.Default.copy(
      capitalization = KeyboardCapitalization.Words
    ),
    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
  )
}

@Composable
fun TagColors(selectedColor: MutableState<Color>) {
  LazyRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 16.dp)
  ) {

    items(UIConstants.tagColors) { color ->
      val modifier = if (selectedColor.value == color) {
        Modifier
          .width(24.dp)
          .height(24.dp)
          .background(color = color)
          .clickable {
            selectedColor.value = color
          }
          .border(1.dp, DarkGray)
      } else {
        Modifier
          .width(24.dp)
          .height(24.dp)
          .background(color = color)
          .clickable {
            selectedColor.value = color
          }
      }

      Box(modifier = modifier)
      Spacer(modifier = Modifier.padding(end = 8.dp))
    }
  }
}

@Composable
fun TagIcons(selectedIcon: MutableState<TagIcon>) {
  LazyRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 16.dp)
  ) {

    items(TagIcon.values()) { tagIcon ->
      val modifier = if (selectedIcon.value == tagIcon) {
        Modifier
          .size(24.dp)
          .clickable {
            selectedIcon.value = tagIcon
          }
          .background(color = Purple80)
      } else {
        Modifier
          .size(24.dp)
          .clickable {
            selectedIcon.value = tagIcon
          }
      }

      Icon(
        painterResource(id = tagIcon.drawableId),
        contentDescription = tagIcon.iconName,
        modifier
      )
      Spacer(modifier = Modifier.padding(end = 8.dp))
    }
  }
}
