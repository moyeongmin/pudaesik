package com.example.bbudaesik.presentation.ui.componenets

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CafeteriaMenu(
    resturantName: List<String>,
    isFavorite: List<Boolean>,
    dorMenuList: Map<String, Map<String, String>>,
    onFavoriteClicked: (Int) -> Unit,
    resMenuList: Map<String, Map<String, Map<String, String>>>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        //기숙사 식단
        items(dorMenuList.size) { index ->
            val resName = dorMenuList.keys.elementAt(index)
            val mealMap = dorMenuList[resName] ?: emptyMap()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = resName,
                    style = MaterialTheme.typography.headlineSmall,
                )

                listOf("조식", "중식", "석식").forEach { mealType ->
                    val menu = mealMap[mealType]
                    if (!menu.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                            text = mealType,
                            style = MaterialTheme.typography.titleLarge,
                            color = lightColorScheme().onSurfaceVariant
                        )
                        TextBox(
                            title = null,
                            content = menu.split("\n")
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }

        // 식당 식단 출력
        Log.d("resare", resMenuList.toString())

        items(resMenuList.size) { index ->
            val resName = resMenuList.keys.elementAt(index)
            val mealMap = resMenuList[resName] ?: emptyMap()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = resName,
                    style = MaterialTheme.typography.headlineSmall,
                )

                listOf("조식", "중식", "석식").forEach { mealType ->
                    val menu = mealMap[mealType]?.get("menu")
                    val mealCost = mealMap[mealType]?.get("mealCost")

                    if (!menu.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                            text = mealType,
                            style = MaterialTheme.typography.titleLarge,
                            color = lightColorScheme().onSurfaceVariant
                        )
                        TextBox(
                            title = mealCost,
                            content = menu.split("\n")
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CafeteriaMenuPreview() {
    val resMenuList = mapOf(
        "금정회관" to mapOf(
            "중식" to mapOf(
                "menu" to "돈까스덮밥\n미소된장국\n배추김치",
                "mealCost" to "4500원"
            ),
            "석식" to mapOf(
                "menu" to "짜장밥\n계란국\n단무지\n배추김치",
                "mealCost" to "4300원"
            )
        )
    )

    val dorMenuList = mapOf(
        "자유관" to mapOf(
            "조식" to "잡곡밥\n미역국\n닭볶음탕\n배추김치",
            "석식" to "흑미밥\n된장찌개\n제육볶음\n김치"
        )
    )

    CafeteriaMenu(
        resturantName = listOf("금정회관", "자유관"),
        isFavorite = listOf(true, false),
        dorMenuList = dorMenuList,
        onFavoriteClicked = {},
        resMenuList = resMenuList
    )
}
