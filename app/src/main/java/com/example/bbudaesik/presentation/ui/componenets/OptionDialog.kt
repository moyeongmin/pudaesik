package com.example.bbudaesik.presentation.ui.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun OptionDialog(
    shownDialog: () -> Unit,
    onDefaultRegionSelected: (Int) -> Unit, // ✅ 기본 지역만 설정
    initialSelectedIndex: Int
) {
    var selectedIndex by remember { mutableIntStateOf(initialSelectedIndex) }

    AlertDialog(
        onDismissRequest = shownDialog,
        title = { Text("기본 캠퍼스") },
        text = {
            Column {
                listOf("부산", "밀양", "양산").forEachIndexed { idx, region ->
                    Row(
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedIndex == idx,
                            onClick = { selectedIndex = idx }
                        )
                        Text(text = region)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onDefaultRegionSelected(selectedIndex) // ✅ 저장만 요청
                shownDialog()
            }) {
                Text("확인")
            }
        }
    )
}