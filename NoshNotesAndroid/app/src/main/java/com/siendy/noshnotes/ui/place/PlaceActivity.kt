package com.siendy.noshnotes.ui.place

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.ui.navigation.Routes.PLACE_KEY
import com.siendy.noshnotes.ui.theme.NoshNotesTheme
import com.siendy.noshnotes.utils.parcelable

class PlaceActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val place: Place? = intent.parcelable(PLACE_KEY)

    setContent {
      NoshNotesTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting("Android")
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  NoshNotesTheme {
    Greeting("Android")
  }
}