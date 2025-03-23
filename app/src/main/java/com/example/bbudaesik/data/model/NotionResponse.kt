package com.example.bbudaesik.data.model

import com.google.gson.annotations.SerializedName

/** ✅ 식당 데이터 응답 DTO */
data class RestaurantResponse(
    @SerializedName("results") val results: List<RestaurantPage>
)

data class RestaurantPage(
    val properties: RestaurantProperties
)

data class RestaurantProperties(
    @SerializedName("MENU_TYPE") val menuType: RichText, // 식사 종류
    @SerializedName("RESTAURANT_CODE") val restaurantCode: RichText, // 식당 코드
    @SerializedName("MENU_CONTENT") val menuContent: RichText, // 식단 텍스트
    @SerializedName("MENU_TITLE") val menuTitle: RichText, // 식사 종류
)

data class DormitoryResponse(
    val results: List<DormitoryMeal>
)

data class DormitoryMeal(
    val properties: DormitoryProperties
)

data class DormitoryProperties(
    val no: noTitle,          // 기숙사 번호
    val codeNm: RichText,       // 식사 종류
    val mealNm: RichText,       // 식단 텍스트
    val mealDate: RichText      // 식사 날짜
)

data class RichText(
    val rich_text: List<RichTextContent>?
)

data class RichTextContent(
    val plain_text: String? = ""
)

data class noTitle(
    val title: List<RichTextContent>?
)



