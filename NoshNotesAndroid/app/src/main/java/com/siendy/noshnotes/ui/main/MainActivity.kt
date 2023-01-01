package com.siendy.noshnotes.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.siendy.noshnotes.R
import com.siendy.noshnotes.ui.navigation.NavigationEvent
import com.siendy.noshnotes.ui.navigation.Routes.AUTOCOMPLETE_REQUEST_CODE
import com.siendy.noshnotes.ui.navigation.Routes.PLACE_KEY
import com.siendy.noshnotes.ui.place.PlaceActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    observeUiState()

    setContent {
      MainScreen()
    }
  }

  private fun observeUiState() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { uiState ->
          if (uiState.navigationEvent != null && uiState.navigationEvent is NavigationEvent.Place) {
            openPlace(uiState.navigationEvent.place)
          }
        }
      }
    }
  }

  private fun openPlace(place: com.siendy.noshnotes.data.models.Place) {
    startActivity(
      Intent(this@MainActivity, PlaceActivity::class.java).apply {
        this.putExtra(PLACE_KEY, place)
      }
    )
  }

  // probably can't do anything about this deprecation unless I implement the autocomplete
  // activity myself
  @Deprecated("Deprecated in Java")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
      when (resultCode) {
        Activity.RESULT_OK -> {
          data?.let {
            viewModel.getPlaceFromAutocomplete(it)
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
