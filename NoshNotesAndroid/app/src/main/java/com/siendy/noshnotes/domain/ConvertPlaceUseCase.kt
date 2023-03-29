package com.siendy.noshnotes.domain

import com.google.android.libraries.places.api.model.Place
import com.siendy.noshnotes.data.models.LatLong

class ConvertPlaceUseCase {
  operator fun invoke(
    googlePlace: Place
  ): com.siendy.noshnotes.data.models.Place {
    return com.siendy.noshnotes.data.models.Place(
      remoteId = googlePlace.id,
      name = googlePlace.name,
      latLong = LatLong(
        latitude = googlePlace.latLng?.latitude,
        longitude = googlePlace.latLng?.longitude,
      ),
      address = googlePlace.address,
      photoMetadata = googlePlace.photoMetadatas.orEmpty().firstOrNull()
    )
  }
}
