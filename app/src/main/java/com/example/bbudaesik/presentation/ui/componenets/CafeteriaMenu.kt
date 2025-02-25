package com.example.bbudaesik.presentation.ui.componenets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bbudaesik.R

@Composable
fun CateteriaMenu(
    resturantName: List<String>,
    isFavorite: List<Boolean>,
    menuList: List<List<String>>,
    onFavoriteClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        items(resturantName.size) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = resturantName[index],
                    style = MaterialTheme.typography.headlineSmall,
                )
                IconButton(
                    modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp),
                    onClick = { onFavoriteClicked(index) }
                ) {
                    if (isFavorite[index]) {
                        Icon(

                            imageVector = ImageVector.vectorResource(R.drawable.star_filled),
                            contentDescription = "favorite",
                            tint = Color.Unspecified,
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.star_filled),
                            contentDescription = "favorite",
                            tint = lightColorScheme().onSurfaceVariant,
                        )
                    }
                }
            }
            menuList.forEach {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                    text = "조식",
                    style = MaterialTheme.typography.titleLarge,
                    color = lightColorScheme().onSurfaceVariant
                )
                TextBox(
                    title = "정식 - 4000원",
                    content = it
                )
                Spacer(modifier = Modifier.padding(10.dp))
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
        menuList = listOf(
            listOf("밥", "김치", "김치찌개", "두부조림", "계란후라이", "우유"),
            listOf("밥", "김치", "김치찌개", "두부조림", "계란후라이", "우유"),
        ),
        onFavoriteClicked = {},
    )
}