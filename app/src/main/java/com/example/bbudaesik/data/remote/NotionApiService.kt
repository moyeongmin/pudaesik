package com.example.bbudaesik.data.remote

import com.example.bbudaesik.data.model.NotionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface NotionApiService {
    @POST("v1/databases/{database_id}/query")
    suspend fun queryDatabase(
        @Path("database_id") databaseId: String,
        @Header("Authorization") token: String,
        @Header("Notion-Version") version: String = "2022-06-28",
        @Body body: Map<String, Any>
    ): NotionResponse
}