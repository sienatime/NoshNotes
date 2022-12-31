package com.siendy.noshnotes

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.flow.Flow

class TagsRepository {

  fun getTags(): Flow<Result<List<Tag>>> {
    return FirebaseRealTimeDatabaseDataSource().getTags()
  }
}
