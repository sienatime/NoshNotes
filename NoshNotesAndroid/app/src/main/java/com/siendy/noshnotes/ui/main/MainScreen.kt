@file:OptIn(ExperimentalMaterial3Api::class)

package com.siendy.noshnotes.ui.main

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.components.AllTags
import com.siendy.noshnotes.ui.components.ConfirmDeletePlaceDialog
import com.siendy.noshnotes.ui.main.map.PlacesMap
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.place.PlaceScreen
import com.siendy.noshnotes.ui.theme.NoshNotesTheme

@Composable
fun App(
  rootNavController: NavHostController,
  mainViewModel: MainViewModel
) {
  NoshNotesTheme {
    NavHost(
      navController = rootNavController,
      startDestination = Routes.MAIN,
      route = "root"
    ) {
      composable(Routes.MAIN) {
        MainScreen(
          rootNavController,
          mainViewModel
        )
      }
      composable(
        "place/new?remoteId={remoteId}",
        arguments = listOf(navArgument("remoteId") { type = NavType.StringType })
      ) { backStackEntry ->
        PlaceScreen(
          placeRemoteId = backStackEntry.arguments?.getString("remoteId"),
          rootNavController = rootNavController
        )
      }
      composable(
        "place/{placeId}",
        arguments = listOf(navArgument("placeId") { type = NavType.StringType })
      ) { backStackEntry ->
        PlaceScreen(
          placeId = backStackEntry.arguments?.getString("placeId"),
          rootNavController = rootNavController
        )
      }
      dialog(
        "place/{placeId}/delete",
        arguments = listOf(navArgument("placeId") { type = NavType.StringType })
      ) { backStackEntry ->
        ConfirmDeletePlaceDialog(
          navHostController = rootNavController,
          mainViewModel = mainViewModel,
          placeId = backStackEntry.arguments?.getString("placeId")
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainScreen(
  rootNavController: NavHostController? = null,
  mainViewModel: MainViewModel = hiltViewModel()
) {
  val mainTabsNavController = rememberNavController()
  val context = LocalContext.current
  val mainUiState by mainViewModel.uiState.collectAsState()

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
    bottomBar = { BottomNavigationBar(mainTabsNavController) },
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = { AddPlaceFAB(context, mainViewModel) },
    content = { padding ->
      MainContent(
        padding,
        mainTabsNavController,
        mainUiState,
        rootNavController,
        mainViewModel
      )
    }
  )
}

@Composable
fun MainContent(
  padding: PaddingValues,
  navController: NavHostController,
  mainUiState: MainUiState,
  rootNavController: NavHostController?,
  mainViewModel: MainViewModel = hiltViewModel()
) {
  Box(modifier = Modifier.padding(padding)) {

    Column(
      modifier = Modifier.padding(
        horizontal = 16.dp,
        vertical = 24.dp
      )
    ) {
      mainUiState.allTagsState?.let {
        AllTags(
          modifier = Modifier.padding(bottom = 8.dp),
          allTagsState = it,
          onTagSelected = { tagState ->
            mainViewModel.onTagSelected(tagState)
          }
        )
      }

      BottomBarNavigationHost(
        bottomBarNavController = navController,
        mainUiState,
        rootNavController,
        mainViewModel
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
  bottomBarNavController: NavHostController,
  mainUiState: MainUiState,
  rootNavController: NavHostController?,
  mainViewModel: MainViewModel
) {
  NavHost(
    bottomBarNavController,
    startDestination = TabDestination.PlacesList.route,
    route = "bottom"
  ) {
    composable(TabDestination.PlacesList.route) {
      PlacesList(mainUiState, rootNavController)
    }
    composable(TabDestination.PlacesMap.route) {
      PlacesMap(mainViewModel)
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
