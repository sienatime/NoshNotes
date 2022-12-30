package com.siendy.noshnotes.domain

import android.app.Activity
import androidx.core.app.ActivityCompat
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.siendy.noshnotes.ui.navigation.Routes

class OpenPlacesAutocompleteUseCase {
  operator fun invoke(activity: Activity) {
    val fieldsToRequest = listOf(
      Place.Field.ID,
      Place.Field.NAME,
      Place.Field.ADDRESS,
      Place.Field.LAT_LNG,
      Place.Field.RATING,
      Place.Field.USER_RATINGS_TOTAL,
      Place.Field.PRICE_LEVEL
    )

    val intent = Autocomplete.IntentBuilder(
      AutocompleteActivityMode.FULLSCREEN,
      fieldsToRequest
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
