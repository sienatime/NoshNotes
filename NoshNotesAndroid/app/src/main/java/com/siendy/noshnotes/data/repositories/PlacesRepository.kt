package com.siendy.noshnotes.data.repositories

import com.google.android.libraries.places.api.model.PhotoMetadata
import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.models.FirebasePlace
import com.siendy.noshnotes.data.models.PhotoWithAttribution
import com.siendy.noshnotes.data.models.Place
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesRepository @Inject constructor(
  private val databaseDataSource: FirebaseRealTimeDatabaseDataSource,
  private val googlePlacesDataSource: GooglePlacesDataSource,
  private val tagsRepository: TagsRepository
) {
  private fun getPlaces(): Flow<List<FirebasePlace>> {
    return databaseDataSource.getPlaces()
  }

  fun updatePlace(place: Place, originalTags: List<String>) {
    databaseDataSource.updatePlace(place, originalTags)
  }

  suspend fun getPlaceByRemoteId(remoteId: String?): Place? {
    return remoteId?.let { googleMapsId ->
      googlePlacesDataSource.getPlaceById(googleMapsId)
    }
  }

  suspend fun getPlaceById(
    placeId: String?
  ): Place? {
    return placeId?.let { id ->
      databaseDataSource.getPlace(id)
    }?.first()?.let {
      getPlaceForFirebasePlace(it)
    }
  }

  suspend fun getPlacesByTagIds(
    tagIds: List<String>
  ): Flow<List<Place>> {
    val tagsSet = tagIds.toSet()

    val places = if (tagsSet.isEmpty()) {
      getPlaces()
    } else {
      getPlaces().map {
        it.filter { firebasePlace ->
          firebasePlace.hasAllTags(tagsSet) && firebasePlace.remoteId != null
        }
      }
    }

    return places.map {
      coroutineScope {
        it.map {
          async {
            getPlaceForFirebasePlace(it)
          }
        }.awaitAll()
      }
    }
  }

  suspend fun getPhoto(photoMetadata: PhotoMetadata): PhotoWithAttribution? {
    return googlePlacesDataSource.getPhoto(photoMetadata)?.let { bitmap ->
      PhotoWithAttribution(
        bitmap,
        photoMetadata.attributions
      )
    }
  }

  private suspend fun getPlaceForFirebasePlace(
    firebasePlace: FirebasePlace
  ): Place {
    val placeWithGoogle = googlePlacesDataSource.getPlaceById(firebasePlace.remoteId!!)
    return placeWithGoogle.copy(
      uid = firebasePlace.uid,
      note = firebasePlace.note,
      tags = firebasePlace.tags.keys.mapNotNull { tagId ->
        tagsRepository.getCachedTag(tagId)
      }
    )
  }

  suspend fun deletePlace(placeId: String) {
    databaseDataSource.deletePlace(placeId)
  }
}
