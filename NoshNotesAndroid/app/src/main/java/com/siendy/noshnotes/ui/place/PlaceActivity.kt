package com.siendy.noshnotes.ui.place

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.navigation.Routes.PLACE_KEY
import com.siendy.noshnotes.utils.parcelable

class PlaceActivity : ComponentActivity() {
  private val viewModel: PlaceViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val place: Place? = intent.parcelable(PLACE_KEY)
    viewModel.setPlace(place)

    setContent {
      PlaceScreen(viewModel)
    }
  }
}
