package com.example.bbudaesik.presentation.ui.mainscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bbudaesik.presentation.ui.componenets.CateteriaMenu
import com.example.bbudaesik.presentation.ui.componenets.LargeChipGroup
import com.example.bbudaesik.presentation.ui.componenets.MediumChipGroup
import com.example.bbudaesik.presentation.ui.theme.BbudaesikTheme
import com.example.bbudaesik.presentation.viewmodel.MainViewModel
import com.example.bbudaesik.utils.WeekInfo
import com.example.bbudaesik.utils.getDate

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val weekInfo = getDate()

    MainScreen(
        selectedCafeteria = uiState.cafeteriaSelectedIndex,
        selectedDate = uiState.selectedDate,
        weekInfo = weekInfo,
        resturantName = uiState.resturantNames,
        menuList = uiState.menuData,
        isLoading = uiState.isLoading,
        error = uiState.error,
        isFavorite = uiState.isFavorite,
        onCafeteriaClicked = viewModel::onCafeteriaClicked,
        onDateClicked = viewModel::onDateClicked,
        onFavoriteClicked = viewModel::onFavoriteClicked,
    )
}

@Composable
fun MainScreen(
    selectedCafeteria: Int,
    selectedDate: Int,
    weekInfo: WeekInfo,
    resturantName: List<String>,
    isFavorite: List<Boolean>,
    menuList: List<String>, // ✅ API에서 가져온 메뉴 리스트 추가
    isLoading: Boolean,
    error: String,
    onCafeteriaClicked: (Int) -> Unit,
    onDateClicked: (Int) -> Unit,
    onFavoriteClicked: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
    ) {
        LargeChipGroup(
            modifier = Modifier,
            listOf("부산", "밀양", "양산"),
            onChipClicked = onCafeteriaClicked,
            selectedIndex = selectedCafeteria,
        )
        MediumChipGroup(
            listOf("일", "월", "화", "수", "목", "금", "토"),
            onChipClicked = onDateClicked,
            subtitles = weekInfo.dates,
            currentDate = weekInfo.currentDate,
            selectedDate = selectedDate,
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            thickness = 1.dp,
            color = Color(0xFFC2C2C4)
        )
        if (isLoading) {
            Text(text = "로딩 중...", modifier = Modifier.padding(16.dp))
        } else if (error.isNotEmpty()) {
            Text(text = "오류: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
        } else {
            CateteriaMenu(
                resturantName = resturantName,
                isFavorite = isFavorite,
                onFavoriteClicked = onFavoriteClicked,
                menuList = listOf(menuList)
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(device = "id:small_phone")
@Composable
private fun MainScreenPreview() {
    BbudaesikTheme {
        Scaffold {
            MainScreen(
                selectedCafeteria = 0,
                selectedDate = 0,
                weekInfo = WeekInfo(
                    currentDate = 0,
                    dates = listOf(0, 1, 2, 3, 4, 5, 6),
                ),
                resturantName = listOf("학생식당", "교직원식당"),
                isFavorite = listOf(true, false),
                onCafeteriaClicked = {},
                onDateClicked = {},
                onFavoriteClicked = {},
                menuList = listOf("김치찌개", "된장찌개", "떡볶이"),
                isLoading = false,
                error = "",
            )
        }
    }
}