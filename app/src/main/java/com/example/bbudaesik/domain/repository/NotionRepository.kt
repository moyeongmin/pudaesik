package com.example.bbudaesik.domain.repository

import com.example.bbudaesik.data.model.NotionResponse

interface NotionRepository {
    suspend fun getFilteredMeals(mealDate: String, mealKindGcd: String, dbKey: String): NotionResponse
}