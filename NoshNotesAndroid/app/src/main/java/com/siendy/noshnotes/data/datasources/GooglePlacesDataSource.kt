package com.siendy.noshnotes.data.datasources

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.domain.ConvertPlaceUseCase
import com.siendy.noshnotes.ui.NoshNotesApplication
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GooglePlacesDataSource(
  private val convertPlaceUseCase: ConvertPlaceUseCase = ConvertPlaceUseCase()
) {
  suspend fun getPlaceById(googleMapsId: String): Place {
    val request = FetchPlaceRequest.newInstance(googleMapsId, fieldsToRequest)

    val placesClient = NoshNotesApplication.instance?.getGooglePlacesClient()
      ?: throw Exception("Application instance is null")

    return suspendCoroutine { continuation ->
      placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
          val googlePlace = response.place
          continuation.resume(convertPlaceUseCase(googlePlace))
        }.addOnFailureListener { exception: Exception ->
          if (exception is ApiException) {
            Log.e("GooglePlacesDataSource", "Place not found: ${exception.message}")
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
      com.google.android.libraries.places.api.model.Place.Field.RATING,
      com.google.android.libraries.places.api.model.Place.Field.USER_RATINGS_TOTAL,
      com.google.android.libraries.places.api.model.Place.Field.PRICE_LEVEL
    )
  }
}
