@file:OptIn(ExperimentalMaterial3Api::class)

package com.siendy.noshnotes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.components.TabBar
import com.siendy.noshnotes.ui.theme.NoshNotesTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun MainScreen() {
  val pagerState = rememberPagerState()

  val pages = listOf(
    stringResource(R.string.list),
    stringResource(R.string.map)
  )

  NoshNotesTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(stringResource(id = R.string.app_name)) }
        )
      },
      floatingActionButtonPosition = FabPosition.End,
      floatingActionButton = {
        FloatingActionButton(
          onClick = {}
        ) {
          Icon(Filled.Add, stringResource(id = R.string.add_place))
        }
      },
      content = {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background,
          content = {
            Column(
              modifier = Modifier.fillMaxHeight()
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f)
              ) {
                Text(text = "helloooooo")

                HorizontalPager(
                  count = pages.size,
                  state = pagerState
                ) { page ->
                  when (page) {
                    0 -> PlacesList()
                    1 -> PlacesMap()
                  }
                }
              }

              TabBar(pages, pagerState)
            }
          }
        )
      }
    )
  }
}
