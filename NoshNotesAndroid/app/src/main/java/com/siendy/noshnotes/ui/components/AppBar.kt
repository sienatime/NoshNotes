package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.siendy.noshnotes.R.string
import com.siendy.noshnotes.ui.theme.NoshNotesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarScreen(
  content: @Composable () -> Unit
) {
  NoshNotesTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(stringResource(id = string.app_name)) }
        )
      },
      content = {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
          content = content
        )
      }
    )
  }
}
