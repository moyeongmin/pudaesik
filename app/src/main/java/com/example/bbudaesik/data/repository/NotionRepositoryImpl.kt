package com.example.bbudaesik.data.repository

import com.example.bbudaesik.data.model.DormitoryResponse
import com.example.bbudaesik.data.model.RestaurantResponse
import com.example.bbudaesik.data.remote.NotionApiService
import com.example.bbudaesik.domain.repository.NotionRepository
import com.google.gson.Gson
import okhttp3.ResponseBody

class NotionRepositoryImpl(
    private val api: NotionApiService,
    private val token: String,
    private val databaseIds: Map<String, String>,
) : NotionRepository {
    private val gson = Gson()

    override suspend fun getMeals(
        mealDate: String,
        dbKey: String,
        restaurantList: List<String>,
    ): Any {
        val databaseId =
            databaseIds[dbKey] ?: throw IllegalArgumentException("Invalid database key: $dbKey")

        val filter =
            when (dbKey) {
                "RESTAURANT" -> {
                    val orConditions =
                        restaurantList.map { name ->
                            mapOf(
                                "property" to "RESTAURANT_CODE",
                                "rich_text" to mapOf("equals" to name),
                            )
                        }

                    mapOf(
                        "and" to
                            listOf(
                                mapOf(
                                    "property" to "MENU_DATE",
                                    "rich_text" to mapOf("equals" to mealDate),
                                ),
                                mapOf("or" to orConditions),
                            ),
                    )
                }

                "DORMITORY" -> {
                    val orConditions =
                        restaurantList.map { dormNo ->
                            mapOf(
                                "property" to "no",
                                "rich_text" to mapOf("equals" to dormNo),
                            )
                        }

                    mapOf(
                        "and" to
                            listOf(
                                mapOf(
                                    "property" to "mealDate",
                                    "rich_text" to mapOf("equals" to mealDate),
                                ),
                                mapOf("or" to orConditions),
                            ),
                    )
                }

                else -> throw IllegalArgumentException("Unsupported dbKey: $dbKey")
            }

        val body = mapOf("filter" to filter)

        val responseBody: ResponseBody = api.queryDatabase(databaseId, "Bearer $token", body = body)
        val responseString = responseBody.string()

        return when (dbKey) {
            "RESTAURANT" -> gson.fromJson(responseString, RestaurantResponse::class.java)
            "DORMITORY" -> gson.fromJson(responseString, DormitoryResponse::class.java)
            else -> throw IllegalArgumentException("Unsupported dbKey: $dbKey")
        }
    }
}
