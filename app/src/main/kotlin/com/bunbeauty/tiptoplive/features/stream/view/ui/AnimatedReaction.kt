package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.util.chance
import com.bunbeauty.tiptoplive.common.util.percent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun AnimatedReaction(
    modifier: Modifier = Modifier,
    order: Int
) {
    val minXAmplitude = with(LocalDensity.current) { 4.dp.toPx().roundToInt() }
    val maxXAmplitude = with(LocalDensity.current) { 16.dp.toPx().roundToInt() }
    val xOffsetAnimation = remember { Animatable(0f) }
    val yOffsetAnimation = remember { Animatable(0f) }
    val alphaAnimation = remember { Animatable(1f) }
    var emojiImageId by remember { mutableIntStateOf(R.drawable.img_reaction_heart) }

    LaunchedEffect(Unit) {
        val delayMillis = Random.nextLong(300, 500) * order
        delay(delayMillis)

        while (true) {
            val animationSpec = tween<Float>(
                durationMillis = Random.nextInt(2_000, 3_000),
                delayMillis = Random.nextInt(300, 1_200),
                easing = LinearEasing
            )
            emojiImageId = chance(
                80.percent to R.drawable.img_reaction_heart,
                9.percent to R.drawable.img_reaction_lol,
                9.percent to R.drawable.img_reaction_fire,
                2.percent to R.drawable.img_reaction_hundred
            )
            val xOffsetAnimationJob = launch {
                val amplitude = Random.nextInt(minXAmplitude, maxXAmplitude)
                val startXOffset = Random.nextInt(0, amplitude).toFloat()
                xOffsetAnimation.snapTo(startXOffset)
                xOffsetAnimation.animateTo(
                    targetValue = -amplitude.toFloat(),
                    animationSpec = animationSpec,
                )
                yOffsetAnimation.snapTo(0f)
            }
            val yOffsetAnimationJob = launch {
                yOffsetAnimation.animateTo(
                    targetValue = -500f,
                    animationSpec = animationSpec,
                )
                yOffsetAnimation.snapTo(0f)
            }
            val alphaAnimationJob = launch {
                alphaAnimation.animateTo(
                    targetValue = 0f,
                    animationSpec = animationSpec,
                )
                alphaAnimation.snapTo(1f)
            }
            xOffsetAnimationJob.join()
            yOffsetAnimationJob.join()
            alphaAnimationJob.join()
        }
    }
    if (yOffsetAnimation.value != 0f) {
        Image(
            modifier = modifier
                .size(24.dp)
                .offset {
                    IntOffset(
                        x = -abs(xOffsetAnimation.value.roundToInt()),
                        y = yOffsetAnimation.value.roundToInt()
                    )
                }
                .alpha(alphaAnimation.value),
            painter = painterResource(emojiImageId),
            contentDescription = "reaction"
        )
    }
}

@Preview
@Composable
fun AnimatedReactionPreview() {
    FakeLiveTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            repeat(6) { i ->
                AnimatedReaction(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    order = i
                )
            }
        }
    }
}