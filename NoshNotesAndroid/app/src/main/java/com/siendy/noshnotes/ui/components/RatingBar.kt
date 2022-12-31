package com.siendy.noshnotes.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.siendy.noshnotes.R.color
import com.siendy.noshnotes.R.drawable
import com.siendy.noshnotes.ui.UIConstants

@Composable
fun RatingBar(rating: Double) {
  Row {
    val whole = if (rating >= UIConstants.MAX_RATING) {
      UIConstants.MAX_RATING.toInt()
    } else if (rating <= UIConstants.MIN_RATING) {
      UIConstants.MIN_RATING.toInt()
    } else {
      rating.toInt()
    }
    val remainder = rating % whole

    val ratings = mutableListOf<StarRating>()

    for (i in 1..whole) {
      ratings.add(StarRating.FULL)
    }

    if (remainder in UIConstants.HALF_RATING_MIN..UIConstants.HALF_RATING_MAX) {
      ratings.add(StarRating.HALF)
    }

    if (ratings.size < UIConstants.MAX_RATING) {
      for (i in ratings.size until UIConstants.MAX_RATING.toInt()) {
        ratings.add(StarRating.NONE)
      }
    }

    RatingStars(ratings = ratings)
  }
}

@Composable
fun RatingStars(ratings: List<StarRating>) {
  ratings.forEach {
    when (it) {
      StarRating.NONE -> {
        Icon(
          painterResource(id = drawable.ic_baseline_star_outline_24),
          contentDescription = null,
          tint = colorResource(color.star_blank)
        )
      }
      StarRating.FULL -> {
        Icon(
          painterResource(id = drawable.ic_baseline_star_24),
          contentDescription = null,
          tint = colorResource(color.star_yellow)
        )
      }
      StarRating.HALF -> {
        Icon(
          painterResource(id = drawable.ic_baseline_star_half_24),
          contentDescription = null,
          tint = colorResource(color.star_yellow)
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun RatingBarPreview() {
  Column {
    RatingBar(rating = -4.0)
    RatingBar(rating = 0.0)
    RatingBar(rating = 2.1)
    RatingBar(rating = 3.4)
    RatingBar(rating = 4.6)
    RatingBar(rating = 5.0)
    RatingBar(rating = 6.0)
  }
}
