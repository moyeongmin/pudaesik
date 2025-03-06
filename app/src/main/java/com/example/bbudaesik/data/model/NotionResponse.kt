package com.example.bbudaesik.data.model

import com.google.gson.annotations.SerializedName

/** ✅ 식당 데이터 응답 DTO */
data class RestaurantResponse(
    @SerializedName("results") val results: List<RestaurantPage>
)

data class RestaurantPage(
    @SerializedName("id") val id: String,
    @SerializedName("properties") val properties: RestaurantProperties
)

data class RestaurantProperties(
    @SerializedName("MENU_DATE") val menuDate: RichTextProperty? = null,
    @SerializedName("MENU_TYPE") val menuType: RichTextProperty? = null,
    @SerializedName("BUILDING_NAME") val buildingName: RichTextProperty? = null,
    @SerializedName("MENU_CONTENT") val menuContent: RichTextProperty? = null
)

/** ✅ 기숙사 데이터 응답 DTO */
data class DormitoryResponse(
    @SerializedName("results") val results: List<DormitoryPage>
)

data class DormitoryPage(
    @SerializedName("id") val id: String,
    @SerializedName("properties") val properties: DormitoryProperties
)

data class DormitoryProperties(
    @SerializedName("mealDate") val mealDate: RichTextProperty? = null,
    @SerializedName("codeNm") val codeNm: RichTextProperty? = null,
    @SerializedName("mealNm") val mealNm: RichTextProperty? = null,
    @SerializedName("no") val dormitoryId: RichTextProperty? = null
)

/** ✅ 공통 RichText */
data class RichTextProperty(
    @SerializedName("rich_text") val richText: List<RichTextContent>? = null
)

data class RichTextContent(
    @SerializedName("text") val text: TextContent? = null
)

data class TextContent(
    @SerializedName("content") val content: String? = ""
)
