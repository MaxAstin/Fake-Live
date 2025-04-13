package com.bunbeauty.tiptoplive.common.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold

private const val SelectedButtonId = "SelectedButtonId"
private const val SelectedBackgroundId = "SelectedBackgroundId"

@Composable
fun SegmentedControl(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    selectedContainerColor: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = CircleShape,
    ) {
        LookaheadScope {
            Layout(
                content = {
                    @OptIn(ExperimentalSharedTransitionApi::class)
                    SelectedBackground(
                        modifier = Modifier.animateBounds(this),
                        selectedBackgroundColor = selectedContainerColor
                    )
                    content()
                },
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .padding(4.dp)
                    .selectableGroup(),
            ) { measurables, constraints ->
                require(measurables.count { it.layoutId == SelectedButtonId } <= 1) {
                    "Segmented control must have at most one selected button"
                }

                val buttonMeasurables = measurables.filter { it.layoutId != SelectedBackgroundId }
                val buttonWidth = constraints.maxWidth / buttonMeasurables.size
                val buttonConstraints = constraints.copy(minWidth = buttonWidth, maxWidth = buttonWidth)
                val buttonPlaceables = buttonMeasurables.map { it.measure(buttonConstraints) }

                val selectedButtonIndex = buttonMeasurables.indexOfFirst { it.layoutId == SelectedButtonId }
                val selectedBackgroundMeasurable = measurables.first { it.layoutId == SelectedBackgroundId }
                val selectedBackgroundPlaceable = if (selectedButtonIndex >= 0) {
                    selectedBackgroundMeasurable.measure(buttonConstraints)
                } else {
                    null
                }

                layout(
                    width = buttonPlaceables.sumOf { it.width },
                    height = buttonPlaceables.maxOf { it.height },
                ) {
                    selectedBackgroundPlaceable?.placeRelative(x = selectedButtonIndex * buttonWidth, y = 0)
                    buttonPlaceables.forEachIndexed { index, it ->
                        it.placeRelative(x = index * buttonWidth, y = 0)
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedControlButton(
    onClick: () -> Unit,
    text: String,
    selected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    modifier: Modifier = Modifier,
) {
    val contentColor by animateColorAsState(
        targetValue = if (selected) selectedColor else unselectedColor,
        label = "ContentColor"
    )
    Box(
        modifier = modifier
            .then(if (selected) Modifier.layoutId(SelectedButtonId) else Modifier)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickableWithoutIndication(onClick),
        contentAlignment = Alignment.Center,
    ) {
        val textStyle = FakeLiveTheme.typography.bodyMedium
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text,
            style = if (selected) textStyle.bold else textStyle,
            color = contentColor,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SelectedBackground(
    selectedBackgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.layoutId(SelectedBackgroundId),
        color = selectedBackgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {}
}