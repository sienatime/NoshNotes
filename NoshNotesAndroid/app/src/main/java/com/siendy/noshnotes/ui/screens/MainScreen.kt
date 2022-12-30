@file:OptIn(ExperimentalMaterial3Api::class)

package com.siendy.noshnotes.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.navigation.TabDestination
import com.siendy.noshnotes.ui.theme.NoshNotesTheme

@Composable
@Preview
fun MainScreen() {
  val navController = rememberNavController()

  NoshNotesTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(stringResource(id = R.string.app_name)) }
        )
      },
      bottomBar = { BottomNavigationBar(navController) },
      floatingActionButtonPosition = FabPosition.End,
      floatingActionButton = {
        FloatingActionButton(
          onClick = {}
        ) {
          Icon(Filled.Add, stringResource(id = R.string.add_place))
        }
      },
      content = { padding ->
        Box(modifier = Modifier.padding(padding)) {
          BottomBarNavigation(navController = navController)
        }
      }
    )
  }
}

@Composable
fun BottomBarNavigation(navController: NavHostController) {
  NavHost(navController, startDestination = TabDestination.PlacesList.route) {
    composable(TabDestination.PlacesList.route) {
      PlacesList()
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
