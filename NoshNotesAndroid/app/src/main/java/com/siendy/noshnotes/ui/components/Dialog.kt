package com.siendy.noshnotes.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.R.string

@Composable
fun Dialog(
  onConfirm: () -> Unit,
  navHostController: NavHostController? = null,
  @StringRes confirmTextId: Int,
  content: @Composable () -> Unit
) {
  Surface {
    Column(
      modifier = Modifier.padding(
        start = 24.dp,
        end = 24.dp,
        top = 24.dp,
        bottom = 8.dp
      )
    ) {
      content()
      DialogButtons(
        onConfirm,
        navHostController,
        confirmTextId
      )
    }
  }
}

@Composable
fun DialogButtons(
  onConfirm: () -> Unit,
  navController: NavHostController? = null,
  @StringRes confirmTextId: Int
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 16.dp),
    horizontalArrangement = Arrangement.End
  ) {
    Button(
      modifier = Modifier.padding(end = 8.dp),
      onClick = {
        navController?.navigateUp()
      }
    ) {
      Text(stringResource(id = string.cancel).uppercase())
    }

    Button(onClick = {
      onConfirm()
      navController?.navigateUp()
    }) {
      Text(stringResource(id = confirmTextId).uppercase())
    }
  }
}

@Preview
@Composable
fun DialogExample() {
  Dialog(
    { },
    null,
    R.string.save
  ) {
    Text("this is my dialog")
  }
}
