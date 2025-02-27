package com.example.bbudaesik.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbudaesik.domain.repository.NotionRepository
import com.example.bbudaesik.utils.WeekInfo
import com.example.bbudaesik.utils.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cafeteriaSelectedIndex: Int = 0,
    val selectedDate: Int = 0,
    val weekInfo: WeekInfo = getDate(),
    val resturantNames: List<String> = listOf("금정회관학생식당", "학생회관밀양학생식당", "편의동2층양산식당"),
    val isFavorite: List<Boolean> = listOf(false, true, false),
    val menuData: List<String> = emptyList(),
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
        0 to listOf("금정회관학생식당", "금정회관교직원식당", "샛벌회관식당"),
        1 to listOf("학생회관밀양학생식당", "학생회관밀양교직원식당"),
        2 to listOf("편의동2층양산식당")
    )

    init {
        val initialDate = _uiState.value.weekInfo.currentDate
        _uiState.update { it.copy(selectedDate = initialDate) }
        fetchRestaurantData(initialDate, 0) // ✅ 앱 실행 시 기본값 (부산 지역)
    }

    /** ✅ 사용자가 지역을 선택했을 때 */
    fun onCafeteriaClicked(index: Int) {
        _uiState.update { it.copy(cafeteriaSelectedIndex = index) }
        fetchRestaurantData(_uiState.value.selectedDate, index)
    }

    /** ✅ 사용자가 날짜를 선택했을 때 */
    fun onDateClicked(index: Int) {
        _uiState.update { it.copy(selectedDate = index) }
        fetchRestaurantData(index, _uiState.value.cafeteriaSelectedIndex)
    }

    /** ✅ Notion API에서 식당 데이터를 가져오는 함수 */
    private fun fetchRestaurantData(selectedDate: Int, regionIndex: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            try {
                val selectedRegion = regionMap[regionIndex] ?: "부산"
                val restaurants = restaurantMap[regionIndex] ?: listOf("금정회관학생식당")
                val mealDate = _uiState.value.weekInfo.dates[selectedDate].toString()

                val menus = mutableListOf<String>()

                for (restaurant in restaurants) {
                    val response = repository.getFilteredMeals(mealDate, restaurant, "restaurant")
                    val menuList = response.results.map { it.properties.mealNm?.rich_text?.firstOrNull()?.text?.content ?: "메뉴 없음" }
                    menus.addAll(menuList)
                }

                _uiState.update { it.copy(isLoading = false, menuData = menus) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "데이터를 불러오는 데 실패했습니다.") }
            }
        }
    }

    fun onFavoriteClicked(index: Int) {
        _uiState.update { it.copy(isFavorite = it.isFavorite.mapIndexed { i, b -> if (i == index) !b else b }) }
    }

}