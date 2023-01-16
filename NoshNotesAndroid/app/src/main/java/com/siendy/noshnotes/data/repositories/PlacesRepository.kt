package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.models.FirebasePlace
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PlacesRepository {

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

  suspend fun getPlacesByTagIds(
    tagIds: List<String>,
    allTagsMap: Map<String, Tag>
  ): Flow<List<Place>> {
    val tagsSet = tagIds.toSet()

    if (tagsSet.isEmpty()) {
      return flowOf(emptyList())
    }

    return getPlaces().map {
      it.filter { firebasePlace ->
        firebasePlace.hasAllTags(tagsSet)
      }
    }.map {
      it.mapNotNull { firebasePlace ->
        firebasePlace.remoteId?.let { googleMapsId ->
          val placeWithGoogle = googlePlacesDataSource.getPlaceById(googleMapsId)
          placeWithGoogle.copy(
            note = firebasePlace.note,
            tags = firebasePlace.tags.keys.mapNotNull { tagId ->
              allTagsMap[tagId]
            }
          )
        }
      }
    }
  }
}
