package com.example.bbudaesik.presentation.ui.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextBox(
    title: String?,
    content: List<String>,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .shadow(3.dp, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(lightColorScheme().onPrimary)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.padding(20.dp))
        if(title!=null) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        content.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
    }
}

@Preview
@Composable
private fun TextBoxPreview() {
    TextBox(
        title = "Title",
        content = listOf("content1", "content2", "content3")
    )
}