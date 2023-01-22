package com.siendy.noshnotes.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.repositories.TagsRepository
import com.siendy.noshnotes.ui.theme.DarkGray
import com.siendy.noshnotes.ui.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewTagDialog(
  parentNavController: NavHostController? = null
) {
  val nameValue = remember { mutableStateOf(TextFieldValue()) }
  val selectedColor = remember { mutableStateOf(Gray) }

  Surface {
    Column(
      modifier = Modifier.padding(
        start = 24.dp,
        end = 24.dp,
        top = 32.dp,
        bottom = 8.dp
      )
    ) {
      TextField(
        value = nameValue.value,
        onValueChange = { nameValue.value = it },
        placeholder = {
          Text(text = stringResource(id = R.string.new_tag_name))
        }
      )

      val colors: List<Color> = listOf(
        Gray,
        Color(0xFFF8BBD0),
        Color(0xFFFFCC80),
        Color(0xFFFFF59D),
        Color(0xFFDCEDC8),
        Color(0xFFB2DFDB),
        Color(0xFFB2EBF2),
        Color(0xFFBBDEFB),
        Color(0xFFE1BEE7),
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
      ) {

        colors.forEach { color ->

          val modifier = if (selectedColor.value == color) {
            Modifier
              .width(24.dp)
              .height(24.dp)
              .background(color = color)
              .clickable {
                selectedColor.value = color
                Log.d("SIENALOG", "${selectedColor.value}")
              }.border(1.dp, DarkGray)
          } else {
            Modifier
              .width(24.dp)
              .height(24.dp)
              .background(color = color)
              .clickable {
                selectedColor.value = color
                Log.d("SIENALOG", "${selectedColor.value}")
              }
          }

          Box(modifier = modifier)
          Spacer(modifier = Modifier.padding(end = 8.dp))
        }
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        horizontalArrangement = Arrangement.End
      ) {
        Button(
          modifier = Modifier.padding(end = 8.dp),
          onClick = {
            parentNavController?.navigateUp()
          }
        ) {
          Text(stringResource(id = R.string.cancel).uppercase())
        }

        Button(onClick = {
          TagsRepository().addTag(
            Tag(
              name = nameValue.value.text
            )
          )
          parentNavController?.navigateUp()
        }) {
          Text(stringResource(id = R.string.save).uppercase())
        }
      }
    }
  }
}
