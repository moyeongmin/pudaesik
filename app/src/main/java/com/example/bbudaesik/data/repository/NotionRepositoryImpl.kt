package com.example.bbudaesik.data.repository

import android.util.Log
import com.example.bbudaesik.data.model.DormitoryResponse
import com.example.bbudaesik.data.model.RestaurantResponse
import com.example.bbudaesik.data.remote.NotionApiService
import com.example.bbudaesik.domain.repository.NotionRepository
import com.example.bbudaesik.utils.BuildingInfo
import com.google.gson.Gson
import okhttp3.ResponseBody

class NotionRepositoryImpl(
    private val api: NotionApiService,
    private val token: String,
    private val databaseIds: Map<String,String>,
) : NotionRepository {
    private val gson = Gson()

    override suspend fun getMeals(mealDate: String, dbKey: String): Any {
        val databaseId = databaseIds[dbKey] ?: throw IllegalArgumentException("Invalid database key: $dbKey")

        val body = when (dbKey) {
            "RESTAURANT" -> mapOf(
                "filter" to mapOf(
                    "property" to "MENU_DATE",
                    "rich_text" to mapOf("equals" to mealDate)
                )
            )
            "DORMITORY" -> mapOf(
                "filter" to mapOf(
                    "property" to "mealDate",
                    "rich_text" to mapOf("equals" to mealDate)
                )
            )
            else -> throw IllegalArgumentException("Unsupported dbKey: $dbKey")
        }

        Log.d("NotionRepositoryImpl", "$dbKey 요청 데이터: ${Gson().toJson(body)}")

        val responseBody: ResponseBody = api.queryDatabase(databaseId, "Bearer $token", body = body)
        val responseString = responseBody.string() // JSON String 변환

        return when (dbKey) {
            "RESTAURANT" -> gson.fromJson(responseString, RestaurantResponse::class.java)
            "DORMITORY" -> gson.fromJson(responseString, DormitoryResponse::class.java)
            else -> throw IllegalArgumentException("Unsupported dbKey: $dbKey")
        }
    }
}