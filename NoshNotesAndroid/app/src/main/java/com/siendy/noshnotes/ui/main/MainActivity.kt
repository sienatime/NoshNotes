package com.siendy.noshnotes.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.navigation.Routes.AUTOCOMPLETE_REQUEST_CODE
import com.siendy.noshnotes.ui.place.PlaceViewModel

class MainActivity : ComponentActivity() {
  private val mainViewModel: MainViewModel by viewModels()
  private val placeViewModel: PlaceViewModel by viewModels()

  private var rootNavController: NavHostController? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      rootNavController = rememberNavController()

      App(
        rootNavController!!,
        mainViewModel,
        placeViewModel
      )
    }
  }

  private fun openPlace(remoteId: String?) {
    remoteId?.let {
      rootNavController?.navigate(Routes.newPlace(remoteId))
    }
  }

  // probably can't do anything about this deprecation unless I implement the autocomplete
  // activity myself
  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
      when (resultCode) {
        Activity.RESULT_OK -> {
          data?.let {
            val googlePlace = Autocomplete.getPlaceFromIntent(it)
            openPlace(googlePlace.id)
          }
        }
        AutocompleteActivity.RESULT_ERROR -> {
          data?.let {
            val status = Autocomplete.getStatusFromIntent(data)
            Log.i("MainActivity", "Failed to get autocompleted place: ${status.statusMessage}")
          }
          Toast
            .makeText(this, R.string.place_error, Toast.LENGTH_SHORT)
            .show()
        }
      }
      return
    }
    super.onActivityResult(requestCode, resultCode, data)
  }
}
