package com.siendy.noshnotes.data.datasources

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.domain.ConvertPlaceUseCase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GooglePlacesDataSource @Inject constructor(
  private val placesClient: PlacesClient,
  private val convertPlaceUseCase: ConvertPlaceUseCase
) {

  suspend fun getPlaceById(googleMapsId: String): Place {
    val googlePlace = getPlace(googleMapsId)

    return convertPlaceUseCase(googlePlace)
  }

  private suspend fun getPlace(googleMapsId: String): com.google.android.libraries.places.api.model.Place {
    val request = FetchPlaceRequest.newInstance(googleMapsId, fieldsToRequest)

    return suspendCoroutine { continuation ->
      placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
          continuation.resume(response.place)
        }.addOnFailureListener { exception: Exception ->
          if (exception is ApiException) {
            Log.e("GooglePlacesDataSource", "Place not found: ${exception.message}")
          }
          continuation.resumeWithException(exception)
        }
    }
  }

  suspend fun getPhoto(photoMetadata: PhotoMetadata): Bitmap? {
    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
      .setMaxWidth(1000)
      .setMaxHeight(600)
      .build()

    return suspendCoroutine { continuation ->
      placesClient.fetchPhoto(photoRequest)
        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
          continuation.resume(fetchPhotoResponse.bitmap)
        }.addOnFailureListener { exception: Exception ->
          if (exception is ApiException) {
            Log.e("GooglePlacesDataSource", "Photo not found: " + exception.message)
          }
          continuation.resumeWithException(exception)
        }
    }
  }

  companion object {
    val fieldsToRequest = listOf(
      com.google.android.libraries.places.api.model.Place.Field.ID,
      com.google.android.libraries.places.api.model.Place.Field.NAME,
      com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
      com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
      com.google.android.libraries.places.api.model.Place.Field.PHOTO_METADATAS
    )
  }
}
