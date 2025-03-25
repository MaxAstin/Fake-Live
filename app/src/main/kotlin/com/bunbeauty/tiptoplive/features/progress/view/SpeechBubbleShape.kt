package com.bunbeauty.tiptoplive.features.progress.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class SpeechBubbleShape(
    private val cornerRadius: Dp,
    private val triangleWidth: Dp,
    private val triangleHeight: Dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val triangleWidthPx = with(density) { triangleWidth.toPx() }
        val triangleHeightPx = with(density) { triangleHeight.toPx() }
        val width = size.width
        val height = size.height

        val path = Path().apply {
            moveTo(cornerRadiusPx, 0f)
            lineTo(width - cornerRadiusPx, 0f)
            arcTo(
                rect = Rect(
                    offset = Offset(width - 2 * cornerRadiusPx, 0f),
                    size = Size(2 * cornerRadiusPx, 2 * cornerRadiusPx)
                ),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(width, height - triangleHeightPx - cornerRadiusPx)
            arcTo(
                rect = Rect(
                    offset = Offset(width - 2 * cornerRadiusPx, height - triangleHeightPx - 2 * cornerRadiusPx),
                    size = Size(2 * cornerRadiusPx, 2 * cornerRadiusPx)
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadiusPx + triangleWidthPx, height - triangleHeightPx)
            lineTo(cornerRadiusPx + triangleWidthPx / 2, height)
            lineTo(cornerRadiusPx, height - triangleHeightPx)
            arcTo(
                rect = Rect(
                    offset = Offset(0f, height - triangleHeightPx - 2 * cornerRadiusPx),
                    size = Size(2 * cornerRadiusPx, 2 * cornerRadiusPx)
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(0f, cornerRadiusPx)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = 2 * cornerRadiusPx,
                    bottom = 2 * cornerRadiusPx
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        return Outline.Generic(path)
    }

}