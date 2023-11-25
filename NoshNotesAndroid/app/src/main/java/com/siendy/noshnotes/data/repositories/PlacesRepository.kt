package com.siendy.noshnotes.data.repositories

import com.google.android.libraries.places.api.model.PhotoMetadata
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.datasources.NoshNotesDataStoreInterface
import com.siendy.noshnotes.data.models.DBPlace
import com.siendy.noshnotes.data.models.PhotoWithAttribution
import com.siendy.noshnotes.data.models.UIPlace
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesRepository @Inject constructor(
  private val databaseDataSource: NoshNotesDataStoreInterface,
  private val googlePlacesDataSource: GooglePlacesDataSource,
  private val tagsRepository: TagsRepository
) {
  private fun getPlaces(): Flow<List<DBPlace>> {
    return databaseDataSource.getPlaces()
  }

  fun updatePlace(place: UIPlace, originalTags: List<String>) {
    databaseDataSource.updatePlace(place, originalTags)
  }

  suspend fun getPlaceByRemoteId(remoteId: String?): UIPlace? {
    return remoteId?.let { googleMapsId ->
      googlePlacesDataSource.getPlaceById(googleMapsId)
    }
  }

  suspend fun getPlaceById(
    placeId: String?
  ): UIPlace? {
    return placeId?.let { id ->
      databaseDataSource.getPlace(id)
    }?.first()?.let {
      getUIPlaceForDBPlace(it)
    }
  }

  suspend fun getPlacesByTagIds(
    tagIds: List<String>
  ): Flow<List<UIPlace>> {
    val tagsSet = tagIds.toSet()

    val places = if (tagsSet.isEmpty()) {
      flowOf(emptyList())
    } else {
      getPlaces().map {
        it.filter { dbPlace ->
          dbPlace.hasAllTags(tagsSet) && dbPlace.remoteId != null
        }
      }
    }

    return places.map {
      coroutineScope {
        it.map {
          async {
            getUIPlaceForDBPlace(it)
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

  private suspend fun getUIPlaceForDBPlace(
    dbPlace: DBPlace
  ): UIPlace {
    val placeWithGoogle = googlePlacesDataSource.getPlaceById(dbPlace.remoteId!!)
    return placeWithGoogle.copy(
      uid = dbPlace.uid,
      note = dbPlace.note,
      tags = dbPlace.tagIds.mapNotNull { tagId ->
        tagsRepository.getCachedTag(tagId)
      }
    )
  }

  suspend fun deletePlace(placeId: String) {
    databaseDataSource.deletePlace(placeId)
  }
}
