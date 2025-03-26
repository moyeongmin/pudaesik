package com.example.bbudaesik.presentation.ui.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bbudaesik.R
import com.example.bbudaesik.presentation.ui.componenets.BDSTopAppBar
import com.example.bbudaesik.presentation.ui.componenets.CafeteriaMenu
import com.example.bbudaesik.presentation.ui.componenets.LargeChipGroup
import com.example.bbudaesik.presentation.ui.componenets.MediumChipGroup
import com.example.bbudaesik.presentation.ui.componenets.OptionDialog
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
    if (uiState.showDialog){
        OptionDialog(
            shownDialog = { viewModel.shownDialog() },
            onDefaultRegionSelected = { viewModel.saveDefaultRegion(it)},
            initialSelectedIndex = uiState.cafeteriaSelectedIndex,
        )
    }

    MainScreen(
        selectedCafeteria = uiState.cafeteriaSelectedIndex,
        selectedDate = uiState.selectedDate,
        weekInfo = weekInfo,
        resturantName = uiState.resturantNames,
        sortedResMenuList = uiState.sortedResMenuList,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onCafeteriaClicked = viewModel::onCafeteriaClicked,
        onDateClicked = viewModel::onDateClicked,
        onFavoriteClicked = viewModel::onFavoriteClicked,
        showDialog = viewModel::showDialog,
    )
}

@Composable
fun MainScreen(
    selectedCafeteria: Int,
    selectedDate: Int,
    weekInfo: WeekInfo,
    resturantName: List<String>,
    sortedResMenuList: List<Triple<String, Boolean, Map<String, Map<String, String>>>>,
    isLoading: Boolean,
    error: String,
    onCafeteriaClicked: (Int) -> Unit,
    onDateClicked: (Int) -> Unit,
    onFavoriteClicked: (String) -> Unit,
    showDialog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
    ) {
        BDSTopAppBar(showDialog)
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
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
            val progress by animateLottieCompositionAsState(composition)
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally),
                        composition = composition,
                        progress = { progress },
                    )
                    Text(text = "식당으로 들어가고 있어요!", modifier = Modifier.padding(16.dp))

                }
            }
        } else if (error.isNotEmpty()) {
            Text(
                text = "오류: $error!\n 다시 시도해주세요!",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            CafeteriaMenu(
                resturantName = resturantName,
                onFavoriteClicked = onFavoriteClicked,
                fullMenuList = sortedResMenuList,
            )
        }
    }
}

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
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
                onCafeteriaClicked = {},
                onDateClicked = {},
                onFavoriteClicked = {},
                sortedResMenuList = listOf(),
                isLoading = false,
                error = "",
                showDialog = {},
            )
        }
    }
}