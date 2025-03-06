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
fun CateteriaMenu(
    resturantName: List<String>,
    isFavorite: List<Boolean>,
    menuList: Map<String, Map<String, String>>,
    onFavoriteClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Log.d("CateteriaMenu", "메뉴 데이터 : $menuList")

        items(menuList.size) { index ->
            val restaurantName = menuList.keys.elementAt(index) // ✅ 식당 이름 가져오기
            val mealTypes = menuList[restaurantName] ?: emptyMap() // ✅ 식사 종류별 메뉴 가져오기
            Log.d("CateteriaMenu", "식당 이름 : $restaurantName, 메뉴 : $mealTypes")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                // ✅ 식당 이름 출력
                Text(
                    text = restaurantName,
                    style = MaterialTheme.typography.headlineSmall,
                )
//                IconButton(
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd)
//                        .size(30.dp),
//                    onClick = { onFavoriteClicked(index) }
//                ) {
//                    if (isFavorite[index]) { 즐겨찾기 기능 추가하기
//                        Icon(
//                            imageVector = ImageVector.vectorResource(R.drawable.star_filled),
//                            contentDescription = "favorite",
//                            tint = Color.Unspecified,
//                        )
//                    } else {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(R.drawable.star_filled), // ✅ 변경: 기본 아이콘 추가
//                            contentDescription = "favorite",
//                            tint = lightColorScheme().onSurfaceVariant,
//                        )
//                    }
//                }
                // ✅ 조식, 중식, 석식 메뉴 출력
                listOf("조식", "중식", "석식").forEach { mealType ->
                    mealTypes[mealType]?.let { menuContent ->
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                            text = mealType,
                            style = MaterialTheme.typography.titleLarge,
                            color = lightColorScheme().onSurfaceVariant
                        )
                        TextBox(
                            title = "정식 - 4000원", // ✅ 가격 데이터 없으므로 기본값 유지
                            content = menuContent.split("\n"), // ✅ 줄바꿈 기준으로 메뉴 나누기
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
    CateteriaMenu(
        resturantName = listOf("학생식당", "교직원식당"),
        isFavorite = listOf(true, false),
        menuList = mapOf(
        ),
        onFavoriteClicked = {},
    )
}