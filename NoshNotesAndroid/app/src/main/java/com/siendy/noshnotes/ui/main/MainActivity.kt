package com.siendy.noshnotes.ui.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.siendy.noshnotes.ui.navigation.Routes.AUTOCOMPLETE_REQUEST_CODE

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    initializeGooglePlaces()

    setContent {
      MainScreen()
    }
  }

  private fun initializeGooglePlaces() {
    val appInfo = applicationContext.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    val metadata = appInfo.metaData

    metadata.getString("com.google.android.geo.API_KEY")?.let { apiKey ->
      Places.initialize(applicationContext, apiKey)
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
      when (resultCode) {
        Activity.RESULT_OK -> {
          data?.let {
            val place = Autocomplete.getPlaceFromIntent(data)
            Log.i("MainActivity", "Place: ${place.name}, ${place.id}")
          }
        }
        AutocompleteActivity.RESULT_ERROR -> {
          // TODO: Handle the error.
          data?.let {
            val status = Autocomplete.getStatusFromIntent(data)
            Log.i("MainActivity", status.statusMessage ?: "")
          }
        }
        Activity.RESULT_CANCELED -> {
          // The user canceled the operation.
        }
      }
      return
    }
    super.onActivityResult(requestCode, resultCode, data)
  }
}
