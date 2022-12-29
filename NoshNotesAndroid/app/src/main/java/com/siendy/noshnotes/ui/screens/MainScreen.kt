package com.siendy.noshnotes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.components.AppBarScreen
import com.siendy.noshnotes.ui.components.TabBar

@OptIn(ExperimentalPagerApi::class)
@Composable
@Preview
fun MainScreen() {
  val pagerState = rememberPagerState()

  val pages = listOf(
    stringResource(R.string.list),
    stringResource(R.string.map)
  )

  AppBarScreen {
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
}
