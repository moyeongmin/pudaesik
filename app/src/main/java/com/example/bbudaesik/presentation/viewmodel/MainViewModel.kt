package com.example.bbudaesik.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bbudaesik.domain.repository.NotionRepository
import com.example.bbudaesik.utils.WeekInfo
import com.example.bbudaesik.utils.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cafeteriaSelectedIndex: Int = 0,
    val selectedDate: Int = 0,
    val weekInfo: WeekInfo = getDate(),
    val resturantNames: List<String> = listOf("식당1", "식당2", "식당3"),
    val isFavorite: List<Boolean> = listOf(false, true, false),
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NotionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        _uiState.update { it.copy(selectedDate = _uiState.value.weekInfo.currentDate) }
    }

    fun onCafeteriaClicked(index: Int) {
        _uiState.update { it.copy(cafeteriaSelectedIndex = index) }
    }

    fun onDateClicked(index: Int) {
        _uiState.update { it.copy(selectedDate = index) }
        Log.d("MainViewModel", uiState.value.weekInfo.currentDate.toString())
    }

    fun onFavoriteClicked(index: Int) {
        _uiState.update { it.copy(isFavorite = it.isFavorite.mapIndexed { i, b -> if (i == index) !b else b }) }
    }

}