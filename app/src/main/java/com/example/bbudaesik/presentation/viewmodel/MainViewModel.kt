package com.example.bbudaesik.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbudaesik.data.model.DormitoryResponse
import com.example.bbudaesik.data.model.RestaurantResponse
import com.example.bbudaesik.domain.repository.NotionRepository
import com.example.bbudaesik.utils.BuildingInfo
import com.example.bbudaesik.utils.WeekInfo
import com.example.bbudaesik.utils.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cafeteriaSelectedIndex: Int = 0,
    val selectedDate: Int = 0,
    val weekInfo: WeekInfo = getDate(),
    val resturantNames: List<String> = listOf("금정회관학생식당", "학생회관밀양학생식당", "편의동2층양산식당"),
    val isFavorite: List<Boolean> = listOf(false, true, false),
    val dorMenuData: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
    val resMenuData: MutableMap<String, MutableMap<String, MutableMap<String, String>>> = mutableMapOf()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NotionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    private val regionMap = mapOf(
        0 to "부산",
        1 to "밀양",
        2 to "양산"
    )

    private val restaurantMap = mapOf(
        0 to listOf("PG002", "PG001", "PS001", "PH002"),//"금정회관 학생 식당", "금정회관 교직원 식당", "샛벌회관 식당", "학생회관 학생 식당"
        1 to listOf("M001", "M002"),//"학생회관(밀양) 학생 식당", "학생회관(밀양) 교직원 식당"
        2 to listOf("Y001")//"편의동2층(양산)
    )

    private val dormitoryResMap = mapOf(
        0 to listOf("2", "11", "13"),// 진리관 2 웅비관 11 자유관 13
        1 to listOf("3"), // 비마관 3
        2 to listOf("12") // 행림관 12
    )

    init {
        val initialDate = _uiState.value.weekInfo.currentDate
        _uiState.update { it.copy(selectedDate = initialDate) }
        fetchMenuData() // ✅ 앱 실행 시 기본값 (부산 지역)
    }

    fun onCafeteriaClicked(index: Int) {
        _uiState.update { it.copy(cafeteriaSelectedIndex = index) }
        fetchMenuData()
    }

    fun onDateClicked(index: Int) {
        _uiState.update { it.copy(selectedDate = index) }
        fetchMenuData()
    }
    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormatter.format(calendar.time)
    }

    private fun fetchMenuData() {
        val formattedDate = getFormattedDate()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            try {
                val resMenuDataMap = mutableMapOf<String, MutableMap<String, MutableMap<String, String>>>()
                val dorMenuDataMap = mutableMapOf<String, MutableMap<String, String>>()

                // ✅ 식당 데이터 요청 및 변환
                val restaurantResponse = repository.getMeals(formattedDate, "RESTAURANT",restaurantMap[_uiState.value.cafeteriaSelectedIndex]?: emptyList())
                Log.d("restaurantResponse", "restaurantResponse: $restaurantResponse")
                if (restaurantResponse is RestaurantResponse) {
                    processRestaurantData(restaurantResponse, resMenuDataMap)
                    _uiState.update { it.copy(isLoading = false, resMenuData = resMenuDataMap) }
                }

                // ✅ 기숙사 데이터 요청 및 변환
                val dormitoryResponse = repository.getMeals(formattedDate, "DORMITORY", dormitoryResMap[_uiState.value.cafeteriaSelectedIndex]?: emptyList())
                if (dormitoryResponse is DormitoryResponse) {
                    processDormitoryData(dormitoryResponse, dorMenuDataMap)
                    _uiState.update { it.copy(isLoading = false, dorMenuData = dorMenuDataMap) }
                }

            } catch (e: Exception) {
                Log.e("MainViewModel", "fetchMenuData 오류 발생: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "데이터를 불러오는 데 실패했습니다.") }
            }
        }
    }

    private fun processRestaurantData(response: RestaurantResponse, resDataMap: MutableMap<String, MutableMap<String, MutableMap<String, String>>>) {
        response.results.forEach { result ->
            val resCode = result.properties.restaurantCode.rich_text?.firstOrNull()?.plain_text ?: ""
            val resName = BuildingInfo.resCodeToNm[resCode] ?: "식당 정보 없음"
            val mealType = result.properties.menuType.rich_text?.firstOrNull()?.plain_text?: ""// 조식 중식 석식 여부
            val mealTypeText = BuildingInfo.menuTimeCodes[mealType]?: "식사 정보 없음"
            val mealCost = result.properties.menuTitle.rich_text?.firstOrNull()?.plain_text?: ""
            val menuContent = result.properties.menuContent.rich_text?.firstOrNull()?.plain_text?: "메뉴 정보 없음"

            Log.d("RestaurantMeal", "[$resName][$mealTypeText][$mealCost] : $menuContent")

            //여기서 데이터를 순서에 맞게 정렬해서 저장하는 방법을 생각 해 봅시다
            // Pair<식당 이름,Pair<식사 종류,Pair<식사 정보,메뉴 정보>>> 현재는 이 상태
            //식사 정보와 메뉴 정보를 분리해서 저장하자



            resDataMap
                .computeIfAbsent(resName) { mutableMapOf() }
                .computeIfAbsent(mealTypeText) { mutableMapOf() }
                .apply {
                    this["mealCost"] = mealCost
                    this["menu"] = menuContent
                }
        }
    }

    private fun processDormitoryData(response: DormitoryResponse, menuDataMap: MutableMap<String, MutableMap<String, String>>) {
        Log.d("processDormitoryData", "response: $response")
        response.results.forEach { meal ->
            val dormNo = meal.properties.no.title?.firstOrNull()?.plain_text ?:""
            val dormName = BuildingInfo.dorNoToNm[dormNo]?: "기숙사 정보 없음"
            val mealType = meal.properties.codeNm.rich_text?.firstOrNull()?.plain_text ?:""
            val mealDate = meal.properties.mealDate.rich_text?.firstOrNull()?.plain_text ?:""
            val mealContent = meal.properties.mealNm.rich_text?.firstOrNull()?.plain_text ?:""

            Log.d("DormitoryMeal", "$mealDate [$dormName][$mealType] : $mealContent")

            menuDataMap.computeIfAbsent(dormName) { mutableMapOf() }[mealType] = mealContent
        }
    }
    fun onFavoriteClicked(index: Int) {
        _uiState.update { it.copy(isFavorite = it.isFavorite.mapIndexed { i, b -> if (i == index) !b else b }) }
    }

}