package com.mo.bbudaesik.presentation.ui.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MediumChip(
    day: String,
    date: Int,
    currentDate: Int,
) {
    Column(
        modifier =
            Modifier
                .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.titleLarge,
            color = if (currentDate == date) Color.Black else Color.Gray,
        )
        Text(
            text = date.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = if (currentDate == date) Color.Black else Color.Gray,
        )
    }
}
