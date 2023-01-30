package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagsRepository @Inject constructor(
  private val databaseDataSource: FirebaseRealTimeDatabaseDataSource
) {

  fun getTags(): Flow<List<Tag>> {
    return databaseDataSource.getTags()
  }

  fun addTag(tag: Tag) {
    databaseDataSource.addTag(tag)
  }
}
