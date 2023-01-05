@file:OptIn(ExperimentalMaterial3Api::class)

package com.siendy.noshnotes.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Rating
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.place.PlaceActivity
import com.siendy.noshnotes.ui.theme.NoshNotesTheme

@Composable
@Preview
fun MainScreen(
  mainViewModel: MainViewModel = viewModel()
) {
  val navController = rememberNavController()
  val context = LocalContext.current
  val mainUiState by mainViewModel.uiState.collectAsState()

  NoshNotesTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(stringResource(id = R.string.app_name)) },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
          )
        )
      },
      bottomBar = { BottomNavigationBar(navController) },
      floatingActionButtonPosition = FabPosition.End,
      floatingActionButton = { AddPlaceFAB(context, mainViewModel) },
      content = { padding ->
        MainContent(
          padding,
          navController,
          mainUiState,
          context,
          mainViewModel
        )
      }
    )
  }
}

@Composable
fun MainContent(
  padding: PaddingValues,
  navController: NavHostController,
  mainUiState: MainUiState,
  context: Context,
  mainViewModel: MainViewModel = viewModel()
) {
  Box(modifier = Modifier.padding(padding)) {

    Column(
      modifier = Modifier.padding(
        horizontal = 16.dp,
        vertical = 24.dp
      )
    ) {
      Button(
        onClick = {
          val place = Place(
            remoteId = "ChIJDwOJGqu5woAR3tTmF6s8bfE",
            name = "Sonoratown",
            latLong = LatLong(34.0539254, -118.3553033),
            address = "5610 San Vicente Blvd, Los Angeles, CA 90019, USA",
            rating = Rating(total = 76, rating = 4.7),
            priceLevel = 1
          )
          context.startActivity(
            Intent(context, PlaceActivity::class.java).apply {
              this.putExtra(Routes.PLACE_KEY, place)
            }
          )
        },
      ) {
        Text("open place")
      }

      mainUiState.allTagsState?.let {
        AllTags(
          it
        ) { tagState ->
          mainViewModel.onTagSelected(tagState)
        }
      }

      BottomBarNavigationHost(
        navController = navController,
        mainUiState.filteredPlaces
      )
    }
  }
}

@Composable
fun AddPlaceFAB(
  context: Context,
  mainViewModel: MainViewModel
) {
  FloatingActionButton(
    onClick = {
      (context as? Activity)?.let {
        mainViewModel.openPlacesAutocomplete(it)
      }
    }
  ) {
    Icon(Filled.Add, stringResource(id = R.string.add_place_fab))
  }
}

@Composable
fun BottomBarNavigationHost(
  navController: NavHostController,
  filteredPlaces: List<Place>
) {
  NavHost(navController, startDestination = TabDestination.PlacesList.route) {
    composable(TabDestination.PlacesList.route) {
      PlacesList(filteredPlaces)
    }
    composable(TabDestination.PlacesMap.route) {
      PlacesMap()
    }
  }
}

// reference: https://johncodeos.com/how-to-create-bottom-navigation-bar-with-jetpack-compose/
@OptIn(ExperimentalUnitApi::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
  val items = listOf(
    TabDestination.PlacesList,
    TabDestination.PlacesMap
  )
  NavigationBar {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    items.forEach { item ->
      NavigationBarItem(
        icon = {
          Icon(
            painterResource(id = item.iconId),
            contentDescription = null
          )
        },
        label = {
          Text(
            text = stringResource(id = item.titleId).uppercase(),
            fontWeight = FontWeight.Medium,
            letterSpacing = TextUnit(1.25f, TextUnitType.Sp)
          )
        },
        alwaysShowLabel = true,
        selected = currentRoute == item.route,
        onClick = {
          navController.navigate(item.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            navController.graph.startDestinationRoute?.let { route ->
              popUpTo(route) {
                saveState = true
              }
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
          }
        }
      )
    }
  }
}
