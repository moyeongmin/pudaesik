package com.example.bbudaesik.presentation.ui.componenets

import LargeChip
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


@Composable
fun LargeChipGroup(
    modifier: Modifier = Modifier,
    titles: List<String>,
    selectedIndex: Int,
    onChipClicked: (Int) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    var chipPositions by remember { mutableStateOf(List(titles.size) { Pair(0f, 0f) }) }

    val indicatorOffset by animateDpAsState(
        targetValue = calculateIndicatorOffset(selectedIndex, chipPositions, LocalDensity.current),
        label = "IndicatorAnimation"
    )

    Row(
        modifier = modifier.wrapContentSize(),
    ) {
        titles.forEachIndexed { index, title ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onChipClicked(index) }
                    .padding(horizontal = 8.dp)
                    .wrapContentSize()
                    .onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
                        val x = layoutCoordinates.positionInParent().x
                        val width = layoutCoordinates.size.width
                        chipPositions = chipPositions
                            .toMutableList()
                            .also {
                                it[index] = Pair(x, width.toFloat())
                            }
                    }
            ) {
                LargeChip(
                    title = title,
                    isSelected = index == selectedIndex,
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

fun calculateIndicatorOffset(
    index: Int,
    chipPositions: List<Pair<Float, Float>>,
    density: androidx.compose.ui.unit.Density
): Dp {
    if (index < 0 || index >= chipPositions.size) return 0.dp

    val (x, width) = chipPositions[index]
    val centerX = x + (width / 2)

    return with(density) { centerX.toDp() } - 3.dp
}