package com.mo.bbudaesik.domain.repository

interface NotionRepository {
    suspend fun getMeals(
        mealDate: String,
        dbKey: String,
        restaurantList: List<String>,
    ): Any
}
