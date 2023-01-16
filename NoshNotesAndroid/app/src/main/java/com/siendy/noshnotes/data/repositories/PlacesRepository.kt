package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.models.FirebasePlace
import com.siendy.noshnotes.data.models.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlacesRepository() {

  private val databaseDataSource by lazy {
    FirebaseRealTimeDatabaseDataSource()
  }

  private val googlePlacesDataSource by lazy {
    GooglePlacesDataSource()
  }

  fun getPlaces(): Flow<List<FirebasePlace>> {
    return databaseDataSource.getPlaces()
  }

  fun addPlace(place: Place) {
    databaseDataSource.addPlace(place)
  }

  suspend fun getPlaceByRemoteId(remoteId: String?): Place? {
    return remoteId?.let { googleMapsId ->
      googlePlacesDataSource.getPlaceById(googleMapsId)
    }
  }

  fun getPlacesByTagIds(tagIds: List<String>): Flow<List<Place>> {
    val tagsSet = tagIds.toSet()
    return getPlaces().map {
      it.filter { firebasePlace ->
        firebasePlace.hasAllTags(tagsSet)
      }
    }.map {
      it.mapNotNull { firebasePlace ->
        firebasePlace.remoteId?.let { googleMapsId ->
          val placeWithGoogle = googlePlacesDataSource.getPlaceById(googleMapsId)
          placeWithGoogle.copy(
            note = firebasePlace.note
          )
        }
      }
    }
  }
}
