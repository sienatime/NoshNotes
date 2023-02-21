package com.siendy.noshnotes.ui.components

import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.PhotoWithAttribution
import com.siendy.noshnotes.ui.theme.Gray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PlacePhoto(
  height: Dp,
  attributionEndPadding: Dp = 0.dp,
  photoMetadata: PhotoMetadata?,
  loadPhoto: suspend (photoMetadata: PhotoMetadata) -> PhotoWithAttribution?
) {

  var photoWithAttribution by remember { mutableStateOf<PhotoWithAttribution?>(null) }

  photoMetadata?.let { metadata ->
    LaunchedEffect(metadata) {
      withContext(Dispatchers.IO) {
        photoWithAttribution = loadPhoto(metadata)
      }
    }
  }

  val painter: Painter = if (LocalInspectionMode.current) {
    painterResource(id = R.drawable.sonoratown)
  } else if (photoWithAttribution != null && photoWithAttribution?.photo != null) {
    rememberAsyncImagePainter(photoWithAttribution!!.photo)
  } else {
    ColorPainter(Gray)
  }

  Column(modifier = Modifier.fillMaxWidth()) {
    Image(
      painter = painter,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxWidth()
        .height(height)
    )

    when {
      photoWithAttribution == null -> {
        // hold the space while we're loading, but don't render this
        // if the attribution is actually null
        HtmlText(
          "",
          modifier = Modifier.padding(end = attributionEndPadding)
        )
      }
      photoWithAttribution?.attributionHtml != null -> {
        val attribution = String.format(
          stringResource(id = R.string.photo_attribution),
          photoWithAttribution?.attributionHtml
        )

        HtmlText(
          attribution,
          modifier = Modifier.padding(end = attributionEndPadding)
        )
      }
    }
  }
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
  AndroidView(
    modifier = modifier,
    factory = { context -> TextView(context) },
    update = {
      it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
      it.movementMethod = LinkMovementMethod.getInstance()
      it.gravity = Gravity.END
      it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
  )
}

@Preview(showBackground = true)
@Composable
fun PlacePhotoPreview() {
  PlacePhoto(
    height = 180.dp,
    photoMetadata = null,
    loadPhoto = { null }
  )
}
