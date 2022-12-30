package com.siendy.noshnotes.domain

import com.google.android.libraries.places.api.model.Place
import com.siendy.noshnotes.data.models.LatLong
import com.siendy.noshnotes.data.models.PriceLevel
import com.siendy.noshnotes.data.models.Rating

class ConvertPlaceUseCase {
  operator fun invoke(googlePlace: Place): com.siendy.noshnotes.data.models.Place {
    return com.siendy.noshnotes.data.models.Place(
      remoteId = googlePlace.id,
      name = googlePlace.name,
      address = googlePlace.address,
      latLong = LatLong(
        latitude = googlePlace.latLng?.latitude,
        longitude = googlePlace.latLng?.longitude,
      ),
      rating = Rating(
        rating = googlePlace.rating,
        total = googlePlace.userRatingsTotal
      ),
      priceLevel = googlePlace.priceLevel?.let {
        val priceLevels = PriceLevel.values()
        if (it < priceLevels.size - 1) {
          PriceLevel.values()[it]
        } else {
          null
        }
      }
    )
  }
}
