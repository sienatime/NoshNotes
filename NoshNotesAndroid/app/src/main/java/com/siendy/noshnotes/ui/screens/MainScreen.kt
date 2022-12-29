package com.siendy.noshnotes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.siendy.noshnotes.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainScreen() {
  val pagerState = rememberPagerState()
  val coroutineScope = rememberCoroutineScope()

  val pages = listOf(
    stringResource(R.string.list),
    stringResource(R.string.map)
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) }
      )
    },
    content = {
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
      ) {
        Column(
          modifier = Modifier.fillMaxHeight()
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
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

          TabRow(
            // Our selected tab is our current page
            selectedTabIndex = pagerState.currentPage,
            // Override the indicator, using the provided pagerTabIndicatorOffset modifier
            indicator = { tabPositions ->
              TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
              )
            }
          ) {
            // Add tabs for all of our pages
            pages.forEachIndexed { index, title ->
              Tab(
                text = { Text(title) },
                selected = pagerState.currentPage == index,
                onClick = {
                  coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                  }
                }
              )
            }
          }
        }
      }
    }
  )
}
