package com.example.bbudaesik.data.repository

import com.example.bbudaesik.data.model.NotionResponse
import com.example.bbudaesik.data.remote.NotionApiService
import com.example.bbudaesik.domain.repository.NotionRepository

class NotionRepositoryImpl(
    private val api: NotionApiService,
    private val token: String,
    private val databaseIds: Map<String,String>,
) : NotionRepository {

    override suspend fun getFilteredMeals(mealDate: String, mealKindGcd: String, dbKey: String): NotionResponse {
        val databaseId = databaseIds[dbKey] ?: throw IllegalArgumentException("Invalid database key: $dbKey")

        val body = mapOf(
            "filter" to mapOf(
                "and" to listOf(
                    mapOf(
                        "property" to "mealDate",
                        "rich_text" to mapOf("equals" to mealDate)
                    ),
                    mapOf(
                        "property" to "mealKindGcd",
                        "rich_text" to mapOf("equals" to mealKindGcd)
                    )
                )
            )
        )
        return api.queryDatabase(databaseId, "Bearer $token", body = body)
    }
}