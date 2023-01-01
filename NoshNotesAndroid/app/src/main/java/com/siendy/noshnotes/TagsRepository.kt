package com.siendy.noshnotes

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.flow.Flow

class TagsRepository {

  private val databaseDataSource by lazy {
    FirebaseRealTimeDatabaseDataSource()
  }

  fun getTags(): Flow<Result<List<Tag>>> {
    return databaseDataSource.getTags()
  }

  fun writeTag(tag: Tag) {
    databaseDataSource.writeTag(tag)
  }
}
