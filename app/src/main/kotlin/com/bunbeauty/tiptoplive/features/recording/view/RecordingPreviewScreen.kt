package com.bunbeauty.tiptoplive.features.recording.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveScaffold
import com.bunbeauty.tiptoplive.common.ui.components.TopBarWithCloseIcon
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.util.openSharing
import com.bunbeauty.tiptoplive.features.recording.presentation.RecordingPreview
import com.bunbeauty.tiptoplive.features.recording.presentation.RecordingPreviewViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun RecordingPreviewScreen(navController: NavHostController) {
    val viewModel: RecordingPreviewViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: RecordingPreview.Action ->
            viewModel.onAction(action)
        }
    }
    val activity = LocalActivity.current
    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                RecordingPreview.Event.NavigateBack -> {
                    navController.navigateUp()
                }

                is RecordingPreview.Event.OpenSharing -> {
                    activity?.openSharing(uri = event.videoUri)
                }
            }
        }.launchIn(this)
    }

    RecordingPreviewContent(
        state = state,
        onAction = onAction
    )
}

@Composable
fun RecordingPreviewContent(
    state: RecordingPreview.State,
    onAction: (RecordingPreview.Action) -> Unit
) {
    FakeLiveScaffold(
        topBar = {
            TopBarWithCloseIcon(
                onCloseIconClick = {
                    onAction(RecordingPreview.Action.CloseClick)
                }
            )
        },
        bottomButton = {
            FakeLivePrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Share recording",
                onClick = {
                    onAction(RecordingPreview.Action.ShareClick)
                },
                enabled = state.videoContent is RecordingPreview.VideoContent.Success
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 64.dp)
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .background(
                        color = FakeLiveTheme.colors.borderVariant,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(16.dp)
            ) {
                when (state.videoContent) {
                    RecordingPreview.VideoContent.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(screenRatio())
                                .background(
                                    color = FakeLiveTheme.colors.inactive,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center),
                                color = FakeLiveTheme.colors.interactive
                            )
                        }
                    }

                    is RecordingPreview.VideoContent.Success -> {
                        MediaPlayerContent(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxHeight()
                                .aspectRatio(screenRatio())
                                .clip(RoundedCornerShape(12.dp)),
                            videoUri = state.videoContent.videoUri,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun screenRatio(): Float {
    val windowInfo = LocalWindowInfo.current
    return windowInfo.containerSize.width.toFloat() / windowInfo.containerSize.height
}

@Composable
@OptIn(UnstableApi::class)
private fun MediaPlayerContent(
    videoUri: Uri,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var player: Player? by remember {
        mutableStateOf(null)
    }

    LifecycleStartEffect(Unit) {
        player = initializePlayer(
            context = context,
            videoUri = videoUri
        )
        onStopOrDispose {
            player?.apply { release() }
            player = null
        }
    }

    Box(modifier) {
        player?.let {
            PlayerSurface(
                player = it,
                surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                modifier = Modifier.clickableWithoutIndication(onClick = onClick)
            )
        }
    }
}

private fun initializePlayer(
    context: Context,
    videoUri: Uri
): Player {
    return ExoPlayer.Builder(context)
        .build()
        .apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
}

@Preview
@Composable
private fun RecordingPreviewScreenPreview() {
    FakeLiveTheme {
        RecordingPreviewContent(
            state = RecordingPreview.State(
                videoContent = RecordingPreview.VideoContent.Loading
            ),
            onAction = {}
        )
    }
}