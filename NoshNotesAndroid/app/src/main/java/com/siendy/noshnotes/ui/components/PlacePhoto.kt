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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.siendy.noshnotes.R
import com.siendy.noshnotes.data.models.Place

@Composable
fun PlacePhoto(
  place: Place,
  height: Dp,
  attributionEndPadding: Dp = 0.dp
) {
  if (place.photo != null) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Image(
        painter = rememberAsyncImagePainter(place.photo),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .height(height)
      )

      if (place.photoAttributionHtml != null) {
        val attribution = String.format(
          stringResource(id = R.string.photo_attribution),
          place.photoAttributionHtml
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
