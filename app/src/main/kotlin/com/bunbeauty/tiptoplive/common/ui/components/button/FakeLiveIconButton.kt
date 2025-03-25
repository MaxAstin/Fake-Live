package com.bunbeauty.tiptoplive.common.ui.components.button

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.util.rememberMultipleEventsCutter

@Composable
fun FakeLiveIconButton(
    @DrawableRes iconId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = FakeLiveTheme.colors.interactive,
    hasMarker: Boolean = false,
    withBorder: Boolean = true,
) {
    val multipleEventsCutter = rememberMultipleEventsCutter()

    IconButton(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .run {
                if (withBorder) {
                    border(
                        width = 1.dp,
                        color = FakeLiveTheme.colors.interactive,
                        shape = RoundedCornerShape(6.dp)
                    )
                } else {
                    this
                }
            }
            .size(48.dp),
        onClick = {
            multipleEventsCutter.processEvent(onClick)
        }
    ) {
        Box {
            Icon(
                modifier = Modifier
                    .padding(2.dp)
                    .size(24.dp),
                painter = painterResource(iconId),
                tint = iconTint,
                contentDescription = contentDescription
            )
            if (hasMarker) {
                val infiniteTransition = rememberInfiniteTransition(label = "scale transition")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.6f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1000,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale animation"
                )
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .scale(scale)
                        .border(
                            width = 1.dp,
                            color = FakeLiveTheme.colors.background,
                            shape = CircleShape
                        )
                        .padding(1.dp),
                    containerColor = FakeLiveTheme.colors.negative,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FakeLiveIconButtonPreview() {
    FakeLiveIconButton(
        iconId = R.drawable.ic_share,
        contentDescription = "",
        onClick = {},
        hasMarker = true,
    )
}