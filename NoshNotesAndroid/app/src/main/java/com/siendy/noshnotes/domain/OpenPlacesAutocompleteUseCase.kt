package com.siendy.noshnotes.domain

import android.app.Activity
import androidx.core.app.ActivityCompat
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.ui.navigation.Routes

class OpenPlacesAutocompleteUseCase {
  operator fun invoke(activity: Activity) {
    val intent = Autocomplete.IntentBuilder(
      AutocompleteActivityMode.FULLSCREEN,
      GooglePlacesDataSource.fieldsToRequest
    )
      .build(activity)

    ActivityCompat.startActivityForResult(
      activity,
      intent,
      Routes.AUTOCOMPLETE_REQUEST_CODE,
      null
    )
  }
}
