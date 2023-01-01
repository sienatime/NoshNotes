package com.siendy.noshnotes.data.datasources

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siendy.noshnotes.data.models.Tag
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRealTimeDatabaseDataSource {
  private val url = "https://nosh-notes-default-rtdb.firebaseio.com/"
  private val tagReference = "tags"

  private val database: FirebaseDatabase by lazy {
    FirebaseDatabase.getInstance(url)
  }

  private val databaseReference: DatabaseReference by lazy {
    database.reference
  }

  // https://medium.com/swlh/how-to-use-firebase-realtime-database-with-kotlin-coroutine-flow-946fe4cf2cd9
  fun getTags(): Flow<Result<List<Tag>>> = callbackFlow {
    val tagsListener = object : ValueEventListener {
      override fun onCancelled(error: DatabaseError) {
        this@callbackFlow.trySendBlocking(Result.failure(error.toException()))
      }

      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.map { childDataSnapshot ->
          childDataSnapshot.getValue(Tag::class.java)
        }
        this@callbackFlow.trySendBlocking(Result.success(items.filterNotNull()))
      }
    }
    database.getReference(tagReference)
      .addValueEventListener(tagsListener)

    awaitClose {
      database.getReference(tagReference)
        .removeEventListener(tagsListener)
    }
  }

  fun writeTag(tag: Tag) {
    databaseReference.child(tagReference).push().key?.let { key ->

      val childUpdates = hashMapOf<String, Any>(
        "/$tagReference/$key" to tag.toMap()
      )

      databaseReference.updateChildren(childUpdates)
    }
  }
}
