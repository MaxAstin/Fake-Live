package com.bunbeauty.tiptoplive.features.progress

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
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.CloseIcon
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.progress.view.SpeechBubbleShape

@Composable
fun ProgressScreen(navController: NavHostController) {
    val viewModel: ProgressViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Progress.Action ->
            viewModel.onAction(action)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveTheme.colors.background)
            .padding(16.dp)
    ) {
        CloseIcon(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { navController.popBackStack() }
        )

        (state as? Progress.State.Success)?.let { successState ->
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
}

@Composable
private fun CenterBlock(
    state: Progress.State.Success,
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
        Text(
            text = state.emoji,
            style = TextStyle(fontSize = 180.sp),
        )
    }
}

@Composable
private fun ProgressBlock(
    state: Progress.State.Success,
    onAction: (Progress.Action) -> Unit,
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
            Text(
                modifier = Modifier.clickableWithoutIndication(
                    onClick = {
                        onAction(Progress.Action.HideHintClick)
                    }
                ),
                text = state.emoji,
                style = TextStyle(fontSize = 48.sp),
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
                    progress = state.progress,
                    color = FakeLiveTheme.colors.progress,
                    backgroundColor = FakeLiveTheme.colors.inactive,
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${state.points} / ${state.pointsToNextLevel} ${stringResource(R.string.progress_points)}",
                    style = FakeLiveTheme.typography.bodySmall,
                    color = FakeLiveTheme.colors.onBackgroundVariant
                )
            }
        }
    }
}

@Composable
private fun HintBox(
    onAction: (Progress.Action) -> Unit,
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
                    onAction(Progress.Action.HideHintClick)
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
    ProgressScreen(navController = rememberNavController())
}