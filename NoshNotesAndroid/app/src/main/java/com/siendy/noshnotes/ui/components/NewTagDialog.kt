package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.repositories.TagsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewTagDialog(
  parentNavController: NavHostController? = null
) {
  val nameValue = remember { mutableStateOf(TextFieldValue()) }

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

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.End
    ) {
      Button(onClick = {
        parentNavController?.navigateUp()
      }) {
        Text(stringResource(id = R.string.cancel))
      }

      Button(onClick = {
        TagsRepository().addTag(
          Tag(
            name = nameValue.value.text
          )
        )
        parentNavController?.navigateUp()
      }) {
        Text(stringResource(id = R.string.save))
      }
    }
  }
}
