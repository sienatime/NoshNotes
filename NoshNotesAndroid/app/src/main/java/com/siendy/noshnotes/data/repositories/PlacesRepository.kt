package com.siendy.noshnotes.data.repositories

import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.models.Place
import kotlinx.coroutines.flow.Flow

class PlacesRepository {

  private val databaseDataSource by lazy {
    FirebaseRealTimeDatabaseDataSource()
  }

  fun getPlaces(): Flow<Result<List<Place>>> {
    return databaseDataSource.getPlaces()
  }

  fun addPlace(place: Place) {
    databaseDataSource.addPlace(place)
  }
}
