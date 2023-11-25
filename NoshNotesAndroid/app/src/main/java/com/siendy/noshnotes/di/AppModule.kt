package com.siendy.noshnotes.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.siendy.noshnotes.data.datasources.FirebaseRealTimeDatabaseDataSource
import com.siendy.noshnotes.data.datasources.GooglePlacesDataSource
import com.siendy.noshnotes.data.datasources.NoshNotesDataStoreInterface
import com.siendy.noshnotes.data.repositories.TagsRepository
import com.siendy.noshnotes.domain.ConvertPlaceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton
  @Provides
  fun provideTagsRepository(databaseDataSource: NoshNotesDataStoreInterface): TagsRepository {
    return TagsRepository(databaseDataSource)
  }

  @Singleton
  @Provides
  fun provideGooglePlacesClient(@ApplicationContext appContext: Context): PlacesClient {
    return Places.createClient(appContext)
  }

  @Singleton
  @Provides
  fun provideNoshNotesDataSource(): NoshNotesDataStoreInterface {
    return FirebaseRealTimeDatabaseDataSource()
  }

  @Singleton
  @Provides
  fun provideGooglePlacesDataSource(
    placesClient: PlacesClient
  ): GooglePlacesDataSource {
    return GooglePlacesDataSource(
      placesClient,
      ConvertPlaceUseCase()
    )
  }
}
