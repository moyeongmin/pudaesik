package com.example.bbudaesik.presentation.viewmodel

import android.app.Application
import android.content.Context
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
    val favoriteNames: Set<String> = emptySet(),
    val showDialog: Boolean = false,
    val dorMenuData: MutableMap<String, MutableMap<String, String>> = mutableMapOf(),
    val resMenuData: MutableMap<String, MutableMap<String, MutableMap<String, String>>> = mutableMapOf(),
    val sortedResMenuList: List<Triple<String, Boolean, Map<String, Map<String, String>>>> = emptyList(),
)

// 저장한 즐겨찾기가 제대로 반영되지 않음 -> 수정 필요
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NotionRepository,
    application: Application,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState
    private val prefs = application.getSharedPreferences("bbudaesik_prefs", Context.MODE_PRIVATE)//application의 context를 사용하기 때문에 안전

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
        val savedIndex = prefs.getInt("default_region", 0)
        val favoriteSet = prefs.getStringSet("favorite_names", emptySet()) ?: emptySet()
        val initialDate = _uiState.value.weekInfo.currentDate

        _uiState.update {
            it.copy(
                selectedDate = initialDate,
                cafeteriaSelectedIndex = savedIndex,
                favoriteNames = favoriteSet
            )
        }
        fetchMenuData()
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

    fun showDialog() {
        _uiState.update { it.copy(showDialog = true) }
    }

    fun shownDialog() {
        _uiState.update { it.copy(showDialog = false) }
    }

    fun saveDefaultRegion(index: Int) {
        prefs.edit().putInt("default_region", index).apply()
    }

    private fun saveFavoriteNames(favorites: Set<String>) {
        prefs.edit().putStringSet("\\favorite_names\\", favorites).apply()
    }

    private fun getFavoriteNames(): Set<String> {
        return prefs.getStringSet("\\favorite_names\\", emptySet()) ?: emptySet()
    }

    private fun fetchMenuData() {
        val formattedDate = getFormattedDate()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            try {
                val resMenuDataMap = mutableMapOf<String, MutableMap<String, MutableMap<String, String>>>()
                val dorMenuDataMap = mutableMapOf<String, MutableMap<String, String>>()

                val restaurantResponse = repository.getMeals(formattedDate, "RESTAURANT", restaurantMap[_uiState.value.cafeteriaSelectedIndex] ?: emptyList())
                if (restaurantResponse is RestaurantResponse) {
                    processRestaurantData(restaurantResponse, resMenuDataMap)
                    _uiState.update { it.copy(resMenuData = resMenuDataMap) }
                }

                val dormitoryResponse = repository.getMeals(formattedDate, "DORMITORY", dormitoryResMap[_uiState.value.cafeteriaSelectedIndex] ?: emptyList())
                if (dormitoryResponse is DormitoryResponse) {
                    processDormitoryData(dormitoryResponse, dorMenuDataMap)
                    _uiState.update { it.copy(dorMenuData = dorMenuDataMap) }
                }

                updateFullMenuList()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.e("MainViewModel", "fetchMenuData 오류 발생: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "데이터를 불러오는 데 실패했습니다.") }
            }
        }
    }



    private fun updateFullMenuList() {
        val resMenus = _uiState.value.resMenuData.map { (name, map) ->
            Triple(name, isFavorite(name), map)
        }
        val dorMenus = _uiState.value.dorMenuData.map { (name, map) ->
            val wrapped = map.mapValues { mapOf("menu" to it.value) }
            Triple(name, isFavorite(name), wrapped)
        }

        val combined = (resMenus + dorMenus).sortedWith(
            compareByDescending<Triple<String, Boolean, Map<String, Map<String, String>>>> { it.second }
                .thenBy { it.first }
        )
        _uiState.update { it.copy(sortedResMenuList = combined) }
    }

    private fun isFavorite(name: String): Boolean {
        return name in _uiState.value.favoriteNames
    }

    private fun processRestaurantData(response: RestaurantResponse, resDataMap: MutableMap<String, MutableMap<String, MutableMap<String, String>>>) {
        response.results.forEach { result ->
            val resCode = result.properties.restaurantCode.rich_text?.firstOrNull()?.plain_text ?: ""
            val resName = BuildingInfo.resCodeToNm[resCode] ?: "식당 정보 없음"
            val mealType = result.properties.menuType.rich_text?.firstOrNull()?.plain_text ?: ""
            val mealTypeText = BuildingInfo.menuTimeCodes[mealType] ?: "식사 정보 없음"
            val mealCost = result.properties.menuTitle.rich_text?.firstOrNull()?.plain_text ?: ""
            val menuContent = result.properties.menuContent.rich_text?.firstOrNull()?.plain_text ?: "메뉴 정보 없음"

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
        response.results.forEach { meal ->
            val dormNo = meal.properties.no.title?.firstOrNull()?.plain_text ?: ""
            val dormName = BuildingInfo.dorNoToNm[dormNo] ?: "기숙사 정보 없음"
            val mealType = meal.properties.codeNm.rich_text?.firstOrNull()?.plain_text ?: ""
            val mealContent = meal.properties.mealNm.rich_text?.firstOrNull()?.plain_text ?: ""

            menuDataMap.computeIfAbsent(dormName) { mutableMapOf() }[mealType] = mealContent
        }
    }

    fun onFavoriteClicked(name: String) {
        val current = _uiState.value.favoriteNames
        val updated = if (name in current) current - name else current + name
        saveFavoriteNames(updated)
        _uiState.update { it.copy(favoriteNames = updated) }
        updateFullMenuList()
    }
}