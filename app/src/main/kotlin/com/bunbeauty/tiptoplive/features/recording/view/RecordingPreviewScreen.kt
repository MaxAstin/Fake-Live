package com.bunbeauty.tiptoplive.features.recording.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveScaffold
import com.bunbeauty.tiptoplive.common.ui.components.TopBarWithCloseIcon
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun RecordingPreviewScreen() {
    FakeLiveScaffold(
        topBar = {
            TopBarWithCloseIcon(
                onCloseIconClick = {
                    //TODO onAction(StreamReview.Action.CloseClick)
                }
            )
        },
        bottomButton = {
            FakeLivePrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Share recording",
                onClick = {
                    //TODO onAction(StreamReview.Action.DoNotAskClick)
                }
            )
        }
    ) {
        Box(
            Modifier
                .padding(bottom = 64.dp)
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(
                        color = FakeLiveTheme.colors.interactive.copy(0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
                    .background(
                        color = FakeLiveTheme.colors.interactive.copy(0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {}
        }
    }
}

@Preview
@Composable
private fun RecordingPreviewScreenPreview() {
    FakeLiveTheme {
        RecordingPreviewScreen()
    }
}