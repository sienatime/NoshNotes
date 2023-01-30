package com.siendy.noshnotes.domain

import android.app.Activity
import androidx.core.app.ActivityCompat
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.siendy.noshnotes.ui.navigation.Routes
import javax.inject.Inject

class OpenPlacesAutocompleteUseCase @Inject constructor() {
  operator fun invoke(activity: Activity) {
    val intent = Autocomplete.IntentBuilder(
      AutocompleteActivityMode.FULLSCREEN,
      listOf(com.google.android.libraries.places.api.model.Place.Field.ID)
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
