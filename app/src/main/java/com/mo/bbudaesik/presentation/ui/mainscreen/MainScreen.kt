package com.mo.bbudaesik.presentation.ui.mainscreen

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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mo.bbudaesik.R
import com.mo.bbudaesik.presentation.ui.componenets.BDSTopAppBar
import com.mo.bbudaesik.presentation.ui.componenets.CafeteriaMenu
import com.mo.bbudaesik.presentation.ui.componenets.LargeChipGroup
import com.mo.bbudaesik.presentation.ui.componenets.MediumChipGroup
import com.mo.bbudaesik.presentation.ui.componenets.OptionDialog
import com.mo.bbudaesik.presentation.ui.theme.BbudaesikTheme
import com.mo.bbudaesik.presentation.viewmodel.MainContent
import com.mo.bbudaesik.presentation.viewmodel.MainViewModel
import com.mo.bbudaesik.utils.WeekInfo
import com.mo.bbudaesik.utils.getDate

@Composable
fun mainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val weekInfo = getDate()
    if (uiState.showDialog) {
        OptionDialog(
            shownDialog = { viewModel.shownDialog() },
            onDefaultRegionSelected = { viewModel.saveDefaultRegion(it) },
            initialSelectedIndex = uiState.cafeteriaSelectedIndex,
        )
    }

    mainScreen(
        selectedCafeteria = uiState.cafeteriaSelectedIndex,
        selectedDate = uiState.selectedDate,
        weekInfo = weekInfo,
        content = uiState.content,
        onCafeteriaClicked = viewModel::onCafeteriaClicked,
        onDateClicked = viewModel::onDateClicked,
        onFavoriteClicked = viewModel::onFavoriteClicked,
        showDialog = viewModel::showDialog,
    )
}

@Composable
fun mainScreen(
    selectedCafeteria: Int,
    selectedDate: Int,
    content: MainContent,
    weekInfo: WeekInfo,
    onCafeteriaClicked: (Int) -> Unit,
    onDateClicked: (Int) -> Unit,
    onFavoriteClicked: (String) -> Unit,
    showDialog: () -> Unit,
) {
    Column(
        modifier =
            Modifier
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
            color = Color(0xFFC2C2C4),
        )
        when (val content = content) {
            MainContent.Loading -> {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
                val progress by animateLottieCompositionAsState(composition)
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        LottieAnimation(
                            modifier =
                                Modifier
                                    .size(100.dp)
                                    .align(Alignment.CenterHorizontally),
                            composition = composition,
                            progress = { progress },
                        )
                        Text(text = "식당으로 들어가고 있어요!", modifier = Modifier.padding(16.dp))
                    }
                }
            }
            is MainContent.Error ->
                Text(
                    text = "오류: ${content.message}!\n 다시 시도해주세요!",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp),
                )
            is MainContent.Menu ->
                CafeteriaMenu(
                    onFavoriteClicked = onFavoriteClicked,
                    fullMenuList = content.sortedResMenuList,
                )
            MainContent.Empty ->
                Text(
                    text = "선택한 날짜에 식단 정보가 없어요!",
                    modifier = Modifier.padding(16.dp),
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
            mainScreen(
                selectedCafeteria = 0,
                selectedDate = 0,
                weekInfo =
                    WeekInfo(
                        currentDate = 0,
                        dates = listOf(0, 1, 2, 3, 4, 5, 6),
                    ),
                onCafeteriaClicked = {},
                onDateClicked = {},
                onFavoriteClicked = {},
                content = MainContent.Loading,
                showDialog = {},
            )
        }
    }
}
