package com.example.bbudaesik.presentation.ui.componenets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bbudaesik.R

@Composable
fun CafeteriaMenu(
    fullMenuList: List<Triple<String, Boolean, Map<String, Map<String, String>>>>,
    resturantName: List<String>,
    onFavoriteClicked: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        items(fullMenuList) { (resName, isFavorite, mealMap) ->
            val index = resturantName.indexOf(resName)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = resName,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    IconButton(onClick = { onFavoriteClicked(resName) }) {
                        Icon(
                            painter = painterResource(id = if (isFavorite) R.drawable.star_filled else R.drawable.star_empty),
                            contentDescription = "즐겨찾기",
                            tint = if (isFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }

                listOf("조식", "중식", "석식").forEach { mealType ->
                    val menu = mealMap[mealType]?.get("menu")
                        ?: mealMap[mealType]?.get("MENU")
                    val mealCost = mealMap[mealType]?.get("mealCost")

                    if (!menu.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                            text = mealType,
                            style = MaterialTheme.typography.titleLarge,
                            color = lightColorScheme().onSurfaceVariant
                        )
                        TextBox(
                            title = mealCost ?: "정식 - 4000원",
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
    val fullMenuList = listOf(
        Triple(
            "자유관", false, mapOf(
                "조식" to mapOf("menu" to "잡곡밥\n미역국\n닭볶음탕\n배추김치"),
                "석식" to mapOf("menu" to "흑미밥\n된장찌개\n제육볶음\n김치")
            )
        ),
        Triple(
            "금정회관", true, mapOf(
                "중식" to mapOf("menu" to "돈까스덮밥\n미소된장국\n배추김치", "mealCost" to "4500원"),
                "석식" to mapOf("menu" to "짜장밥\n계란국\n단무지\n배추김치", "mealCost" to "4300원")
            )
        )
    )

    CafeteriaMenu(
        fullMenuList = fullMenuList,
        resturantName = listOf("금정회관", "자유관"),
        onFavoriteClicked = {}
    )
}
