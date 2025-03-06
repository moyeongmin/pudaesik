package com.example.bbudaesik.presentation.ui.componenets

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun MediumChipGroup(
    titles: List<String>,
    subtitles: List<Int>,
    selectedDate: Int,
    onChipClicked: (Int) -> Unit,
    currentDate: Int
) {

    val interactionSource = remember { MutableInteractionSource() }
    var chipPositions by remember { mutableStateOf(List(titles.size) { Pair(0f, 0f) }) }
    val selectedIndex = subtitles.indexOf(selectedDate)
    val indicatorOffset by animateDpAsState(
        targetValue = calIndicatorOffset(selectedIndex, chipPositions, LocalDensity.current),
        label = "IndicatorAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        titles.forEachIndexed { index, title ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.8f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { Log.d("test", "index: ${subtitles[index]}");
                        onChipClicked(subtitles[index]) }
                    .onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
                        val x = layoutCoordinates.positionInParent().x
                        val width = layoutCoordinates.size.width
                        chipPositions = chipPositions
                            .toMutableList()
                            .also {
                                it[index] = Pair(x, width.toFloat())
                            }
                    },
                contentAlignment = Alignment.Center
            ) {
                MediumChip(
                    day = title,
                    date = subtitles[index],
                    currentDate = currentDate,
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .offset { IntOffset(indicatorOffset.roundToPx(), 0) }
            .padding(top = 4.dp)
            .size(6.dp)
            .clip(CircleShape)
            .background(Color(0xFF00AAFF))
    )
}

fun calIndicatorOffset(
    index: Int,
    chipPositions: List<Pair<Float, Float>>,
    density: androidx.compose.ui.unit.Density
): androidx.compose.ui.unit.Dp {
    if (index < 0 || index >= chipPositions.size) return 0.dp

    val (x, width) = chipPositions[index]
    val centerX = x + (width / 2)


    return with(density) { centerX.toDp() } - 3.dp
}
