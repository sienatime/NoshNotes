package com.siendy.noshnotes.ui.components

import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabBar(
  pages: List<String>,
  pagerState: PagerState
) {
  TabRow(
    selectedTabIndex = pagerState.currentPage,
    indicator = { tabPositions ->
      TabRowDefaults.Indicator(
        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
      )
    }
  ) {
    val coroutineScope = rememberCoroutineScope()

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
