package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.models.FirebasePlace
import com.siendy.noshnotes.data.models.Place
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

  // TODO i want to actually use Firebase ID first??? but i might not HAVE a firebase ID,
  //  and in that case i need a google ID. i kinda need...both...
  //  so maybe i need like /place/new?googleId=abc OR /place/firebaseid
  //  cuz if i have a firebase id already, i need to fetch firebase first THEN merge with google?
  //  or whatever, i think i already have logic for the opposite but i need to put it somewhere better
  //  (merge Place and GooglePlace)
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
            uid = firebasePlace.uid,
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
