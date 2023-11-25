package com.siendy.noshnotes.data.datasources

import com.siendy.noshnotes.data.models.DBPlace
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.models.UIPlace
import kotlinx.coroutines.flow.Flow

interface NoshNotesDataStoreInterface {
  fun getTags(): Flow<List<Tag>>
  fun getPlaces(): Flow<List<DBPlace>>
  fun getPlace(id: String): Flow<DBPlace?>
  fun addTag(tag: Tag)
  fun updatePlace(place: UIPlace, originalTags: List<String>)
  suspend fun deletePlace(placeId: String)
}
