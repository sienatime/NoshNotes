package com.siendy.noshnotes.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.navigation.Routes
import com.siendy.noshnotes.ui.navigation.Routes.AUTOCOMPLETE_REQUEST_CODE
import com.siendy.noshnotes.ui.navigation.Routes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
  private var rootNavController: NavHostController? = null

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      rootNavController = rememberNavController()

      App(
        rootNavController!!,
        mainViewModel
      )
    }

    getLocationPermission()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.isNotEmpty() &&
          grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
          mainViewModel.onLocationPermissionGranted()
        }
      }
      else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

  private fun getLocationPermission() {
    val locationPermission = ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)

    if (locationPermission == PackageManager.PERMISSION_GRANTED) {
      mainViewModel.onLocationPermissionGranted()
    } else {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
      )
    }
  }
}
