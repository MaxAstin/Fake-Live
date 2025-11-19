package com.bunbeauty.tiptoplive.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.stream.view.ViewersCountUi
import com.bunbeauty.tiptoplive.features.stream.view.ui.StreamHeader

@Composable
fun OnboardingScreen() {
    Box {
        HorizontalPager(
            state = rememberPagerState(
                pageCount = { onboardingContentList.size }
            )
        ) { page ->
            val content = onboardingContentList[page]
            OnboardingPage(
                modifier = Modifier.fillMaxSize(),
                content = content,
            )
        }
        StreamHeader(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.TopEnd),
            image = ImageSource.ResId(data = R.drawable.a1),
            username = stringResource(R.string.onboarding_your_name),
            viewersCount = ViewersCountUi.UpToThousand("990"),
            actionEnabled = true,
            onClose = {},
            onSwitchCamera = {},
            onCameraClick = {},
            onFiltersClick = {},
        )
        FakeLivePrimaryButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.onboarding_get_started),
            onClick = {

            }
        )
    }
}

@Composable
private fun OnboardingPage(
    content: OnboardingContent,
    modifier: Modifier = Modifier,
) {
    Box {
        Image(
            modifier = modifier,
            painter = painterResource(content.imageId),
            contentDescription = "photo",
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            FakeLiveTheme.colors.surface.copy(alpha = 0.1f),
                            FakeLiveTheme.colors.surface.copy(alpha = 0.4f),
                            FakeLiveTheme.colors.surface.copy(alpha = 0.6f),
                            FakeLiveTheme.colors.surface.copy(alpha = 0.7f),
                            FakeLiveTheme.colors.surface.copy(alpha = 0.8f),
                            FakeLiveTheme.colors.surface.copy(alpha = 0.9f),
                            FakeLiveTheme.colors.surface,
                        )
                    ),
                )
                .padding(top = 80.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 120.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(content.titleId),
                style = FakeLiveTheme.typography.titleLarge.bold,
                color = FakeLiveTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(content.textId),
                style = FakeLiveTheme.typography.bodyLarge,
                color = FakeLiveTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    FakeLiveTheme {
        OnboardingScreen()
    }
}