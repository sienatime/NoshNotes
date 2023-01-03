package com.siendy.noshnotes.ui

import android.app.Application
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.FirebaseApp
import com.siendy.noshnotes.data.Constants
import com.siendy.noshnotes.utils.getApplicationInfoCompat

class NoshNotesApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    instance = this
    initializeGooglePlaces()
    initializeFirebase()
  }

  // TODO: DI this
  fun getGooglePlacesClient(): PlacesClient {
    return Places.createClient(this)
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

  companion object {
    var instance: NoshNotesApplication? = null
  }
}
