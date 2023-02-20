package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagsRepository @Inject constructor(
  private val databaseDataSource: FirebaseRealTimeDatabaseDataSource
) {

  private lateinit var tagsCache: Map<String, Tag>

  fun getTags(): Flow<List<Tag>> {
    return databaseDataSource.getTags()
  }

  fun getCachedTag(id: String): Tag? {
    return tagsCache[id]
  }

  fun addTag(tag: Tag) {
    databaseDataSource.addTag(tag)
  }

  fun initCache(tags: List<Tag>) {
    tagsCache = tags.mapNotNull { tag ->
      tag.uid?.let { uid ->
        uid to tag
      }
    }.toMap()
  }
}
