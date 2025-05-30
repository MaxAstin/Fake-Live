package com.bunbeauty.tiptoplive.features.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.progress.view.SpeechBubbleShape

@Composable
fun AwardsScreen() {
    val viewModel: AwardsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Awards.Action ->
            viewModel.onAction(action)
        }
    }

    AwardsContent(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun AwardsContent(
    state: Awards.State,
    onAction: (Awards.Action) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveTheme.colors.background)
            .padding(16.dp)
    ) {
        (state as? Awards.State.Success)?.let { successState ->
            CenterBlock(
                modifier = Modifier.align(Alignment.Center),
                state = successState
            )
            ProgressBlock(
                modifier = Modifier.align(Alignment.BottomCenter),
                state = successState,
                onAction = onAction
            )
        }
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("new_level_animation.json"))
    LottieAnimation(
        modifier = Modifier.fillMaxSize(),
        isPlaying = (state as? Awards.State.Success)?.showNewAwardsAnimation ?: false,
        composition = composition
    )
}

@Composable
private fun CenterBlock(
    state: Awards.State.Success,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = spacedBy(24.dp)
    ) {
        val stringId = if (state.level == 1) {
            R.string.progress_stream_more
        } else {
            R.string.progress_new_level
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(stringId),
            style = FakeLiveTheme.typography.titleLarge.bold,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier.size(240.dp),
            painter = painterResource(id = state.imageId),
            contentDescription = "emoji",
        )
    }
}

@Composable
private fun ProgressBlock(
    state: Awards.State.Success,
    onAction: (Awards.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (state.showHint) {
            HintBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 4.dp),
                onAction = onAction
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clickableWithoutIndication(
                        onClick = {
                            onAction(Awards.Action.EmojiClick)
                        }
                    ),
                painter = painterResource(id = state.imageId),
                contentDescription = "emoji",
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.progress_level, state.level),
                    style = FakeLiveTheme.typography.bodySmall.bold,
                    color = FakeLiveTheme.colors.onBackground
                )
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    progress = { state.progress },
                    color = FakeLiveTheme.colors.progress,
                    trackColor = FakeLiveTheme.colors.inactive,
                    strokeCap = StrokeCap.Round,
                    gapSize = (-12).dp,
                    drawStopIndicator = {}
                )
                Text(
                    text = "${state.points} / ${state.nextLevelPoints} ${stringResource(R.string.progress_points)}",
                    style = FakeLiveTheme.typography.bodySmall,
                    color = FakeLiveTheme.colors.onBackgroundVariant
                )
            }
        }
    }
}

@Composable
private fun HintBox(
    onAction: (Awards.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val bubbleShape = SpeechBubbleShape(
        cornerRadius = 8.dp,
        triangleWidth = 12.dp,
        triangleHeight = 16.dp
    )
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = FakeLiveTheme.colors.onBackground,
                shape = bubbleShape
            )
            .clickableWithoutIndication(
                onClick = {
                    onAction(Awards.Action.HintClick)
                }
            )
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 24.dp),
            text = stringResource(R.string.progress_minute_equals_point),
            style = FakeLiveTheme.typography.bodySmall,
            color = FakeLiveTheme.colors.onBackground
        )
        Icon(
            modifier = Modifier
                .padding(bottom = 16.dp, end = 8.dp)
                .size(16.dp)
                .align(Alignment.CenterEnd),
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "Hide hint",
            tint = FakeLiveTheme.colors.onBackgroundVariant
        )
    }
}

@Preview
@Composable
private fun ProgressScreenPreview() {
    FakeLiveTheme {
        AwardsContent(
            state = Awards.State.Success(
                showNewAwardsAnimation = true,
                showHint = false,
                level = 10,
                imageId = R.drawable.img_crown,
                progress = 1f,
                points = 100,
                nextLevelPoints = 100,
            ),
            onAction = {}
        )
    }
}