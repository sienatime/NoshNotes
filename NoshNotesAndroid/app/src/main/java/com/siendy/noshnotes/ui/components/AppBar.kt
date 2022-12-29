package com.siendy.noshnotes.ui.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.siendy.noshnotes.R

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarScreen() {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) }
      )
    }
  ) {
    // Screen content
  }
}
