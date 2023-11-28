package com.siendy.noshnotes.data.datasources

import com.siendy.noshnotes.data.models.DBPlace
import com.siendy.noshnotes.data.models.KtorPlaceTags
import com.siendy.noshnotes.data.models.KtorPlaces
import com.siendy.noshnotes.data.models.KtorTags
import com.siendy.noshnotes.data.models.SupabasePlace
import com.siendy.noshnotes.data.models.SupabasePlaceTag
import com.siendy.noshnotes.data.models.SupabaseTag
import com.siendy.noshnotes.data.models.Tag
import com.siendy.noshnotes.data.models.UIPlace
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SupabaseDataStore : NoshNotesDataStoreInterface {
  private val key = "SECRET UNTIL RLS ENABLED"

  private val client = HttpClient(OkHttpEngine(OkHttpConfig())) {
    install(Resources)
    install(ContentNegotiation) {
      json()
    }
    defaultRequest {
      url("https://rkdlxrqapgwyxeodnsrb.supabase.co/rest/v1/")
      headers.append("apikey", key)
      headers.append(HttpHeaders.Authorization, "Bearer $key")
      headers.appendIfNameAbsent(HttpHeaders.ContentType, "application/json")
    }
  }

  override fun getTags(): Flow<List<Tag>> {
    return flow {
      val response: HttpResponse = client.get(KtorTags())
      val body: List<SupabaseTag> = response.body()
      emit(body.map { it.toTag() })
    }
  }

  override fun getPlaces(): Flow<List<DBPlace>> {
    return flow {
      val response: HttpResponse = client.get(KtorPlaces(select = PLACES_WITH_TAGS_COLUMNS))
      val body: List<SupabasePlace> = response.body()
      emit(body.map { it.toDBPlace() })
    }
  }

  override fun getPlacesForTags(tagIds: List<String>): Flow<List<DBPlace>> {
    return flow {
      val placeTagsResponse: HttpResponse = client.get(KtorPlaceTags(tag_id = valuesIn(tagIds)))
      val body: List<SupabasePlaceTag> = placeTagsResponse.body()
      val placeIds = body.map { it.placeId.toString() }
      // TODO filter based on only places that returned all tag IDs. maybe like a group by count or something
      val placesResponse = client.get(KtorPlaces(select = "*", id = valuesIn(placeIds)))
      val placesBody: List<SupabasePlace> = placesResponse.body()
      emit(placesBody.map { it.toDBPlace(tagIds = tagIds) })
    }
  }

  override fun getPlace(id: String): Flow<DBPlace?> {
    return flow {
      val placesResponse = client.get(KtorPlaces(select = PLACES_WITH_TAGS_COLUMNS, id = valueEqual(id)))
      val placesBody: List<SupabasePlace> = placesResponse.body()
      emit(placesBody.firstOrNull()?.toDBPlace())
    }
  }

  override fun addTag(tag: Tag) {
    TODO("Not yet implemented")
  }

  override fun updatePlace(place: UIPlace, originalTags: List<String>) {
    TODO("Not yet implemented")
  }

  override suspend fun deletePlace(placeId: String) {
    TODO("Not yet implemented")
  }

  private fun valuesIn(values: List<String>): String {
    return "in.(${values.joinToString(",")})"
  }

  private fun valueEqual(value: String): String {
    return "eq.$value"
  }

  companion object {
    private const val TAGS = "tags"
    private const val PLACES = "places"
    private const val PLACE_TAGS = "place_tags"
    private const val PLACES_WITH_TAGS_COLUMNS = "id,created_at,remote_id,note,place_tags(place_id,tag_id)"
  }
}
