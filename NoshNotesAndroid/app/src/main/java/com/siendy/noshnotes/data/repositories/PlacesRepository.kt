package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.models.FirebasePlace
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesRepository @Inject constructor(
  private val databaseDataSource: FirebaseRealTimeDatabaseDataSource,
  private val googlePlacesDataSource: GooglePlacesDataSource
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
    placeId: String?,
    allTagsMap: Map<String, Tag>
  ): Place? {
    val firebasePlace = placeId?.let { id ->
      databaseDataSource.getPlace(id)
    }?.first()

    return firebasePlace?.remoteId?.let { googleMapsId ->
      val placeWithGoogle = googlePlacesDataSource.getPlaceById(googleMapsId)
      placeWithGoogle.copy(
        uid = firebasePlace.uid,
        note = firebasePlace.note,
        tags = firebasePlace.tags.keys.mapNotNull { tagId ->
          allTagsMap[tagId]
        }
      )
    }
  }

  suspend fun getPlacesByTagIds(
    tagIds: List<String>,
    allTagsMap: Map<String, Tag>
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
            getPlaceForFirebasePlace(it, allTagsMap)
          }
        }.awaitAll()
      }
    }
  }

  private suspend fun getPlaceForFirebasePlace(
    firebasePlace: FirebasePlace,
    allTagsMap: Map<String, Tag>
  ): Place {
    val placeWithGoogle = googlePlacesDataSource.getPlaceById(firebasePlace.remoteId!!)
    return placeWithGoogle.copy(
      uid = firebasePlace.uid,
      note = firebasePlace.note,
      tags = firebasePlace.tags.keys.mapNotNull { tagId ->
        allTagsMap[tagId]
      }
    )
  }

  suspend fun deletePlace(placeId: String) {
    databaseDataSource.deletePlace(placeId)
  }
}
