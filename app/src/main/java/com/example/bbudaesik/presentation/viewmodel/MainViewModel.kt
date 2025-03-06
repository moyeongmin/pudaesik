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
    val menuData: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
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
        0 to listOf("금정회관 학생 식당", "금정회관 교직원 식당", "샛벌회관 식당"),
        1 to listOf("학생회관(밀양) 학생 식당", "학생회관(밀양) 교직원 식당"),
        2 to listOf("편의동2층(양산) 식당")
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
                val menuDataMap = mutableMapOf<String, MutableMap<String, String>>()

                // ✅ 식당 데이터 요청 및 변환
                val restaurantResponse = repository.getMeals(formattedDate, "RESTAURANT")
                if (restaurantResponse is RestaurantResponse) {
                    processRestaurantData(restaurantResponse, menuDataMap)
                }

                // ✅ 기숙사 데이터 요청 및 변환
                val dormitoryResponse = repository.getMeals(formattedDate, "DORMITORY")
                if (dormitoryResponse is DormitoryResponse) {
                    processDormitoryData(dormitoryResponse, menuDataMap)
                }

                _uiState.update { it.copy(isLoading = false, menuData = menuDataMap) }

            } catch (e: Exception) {
                Log.e("MainViewModel", "fetchMenuData 오류 발생: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "데이터를 불러오는 데 실패했습니다.") }
            }
        }
    }

    private fun processRestaurantData(response: RestaurantResponse, menuDataMap: MutableMap<String, MutableMap<String, String>>) {
        response.results.forEach { result ->
            val restaurantName = result.properties.buildingName?.richText?.firstOrNull()?.text?.content ?: "식당 정보 없음"
            val mealType = result.properties.menuType?.richText?.firstOrNull()?.text?.content ?: "알 수 없음"
            val menuContent = result.properties.menuContent?.richText?.firstOrNull()?.text?.content ?: "메뉴 없음"

            menuDataMap.computeIfAbsent(restaurantName) { mutableMapOf() }[mealType] = menuContent
        }
    }

    private fun processDormitoryData(response: DormitoryResponse, menuDataMap: MutableMap<String, MutableMap<String, String>>) {
        response.results.forEach { result ->
            val dormitoryName = "기숙사 ${result.properties.dormitoryId?.richText?.firstOrNull()?.text?.content ?: "정보 없음"}"
            val mealType = result.properties.codeNm?.richText?.firstOrNull()?.text?.content ?: "알 수 없음"
            val menuContent = result.properties.mealNm?.richText?.firstOrNull()?.text?.content ?: "메뉴 없음"

            menuDataMap.computeIfAbsent(dormitoryName) { mutableMapOf() }[mealType] = menuContent
        }
    }
    fun onFavoriteClicked(index: Int) {
        _uiState.update { it.copy(isFavorite = it.isFavorite.mapIndexed { i, b -> if (i == index) !b else b }) }
    }

}