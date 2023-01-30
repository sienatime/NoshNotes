package com.siendy.noshnotes.ui

import android.app.Application
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.siendy.noshnotes.data.Constants
import com.siendy.noshnotes.utils.getApplicationInfoCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoshNotesApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    initializeGooglePlaces()
    initializeFirebase()
  }

  private fun initializeGooglePlaces() {
    val appInfo = applicationContext.packageManager.getApplicationInfoCompat(
      packageName,
      PackageManager.GET_META_DATA
    )
    val metadata = appInfo.metaData

    metadata.getString(Constants.GOOGLE_PLACES_METADATA_KEY)?.let { apiKey ->
      Places.initialize(applicationContext, apiKey)
    }
  }

  private fun initializeFirebase() {
    FirebaseApp.initializeApp(applicationContext)
  }
}
