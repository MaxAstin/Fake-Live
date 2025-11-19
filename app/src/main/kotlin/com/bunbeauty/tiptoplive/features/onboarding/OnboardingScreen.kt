package com.bunbeauty.tiptoplive.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.stream.view.ViewersCountUi
import com.bunbeauty.tiptoplive.features.stream.view.ui.StreamHeader

@Composable
fun OnboardingScreen() {
    Box {
        HorizontalPager(
            state = rememberPagerState(
                pageCount = { 6 }
            )
        ) { page ->
            val imageId = when (page) {
                0 -> R.drawable.img_onboarding_0
                1 -> R.drawable.img_onboarding_1
                2 -> R.drawable.img_onboarding_2
                3 -> R.drawable.img_onboarding_3
                4 -> R.drawable.img_onboarding_4
                else -> R.drawable.img_onboarding_5
            }
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(imageId),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
        }
        StreamHeader(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.TopEnd),
            image = ImageSource.ResId(data = R.drawable.a1),
            username = "username",
            viewersCount = ViewersCountUi.UpToThousand("990"),
            actionEnabled = true,
            onClose = {},
            onSwitchCamera = {},
            onCameraClick = {},
            onFiltersClick = {},
        )
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    FakeLiveTheme {
        OnboardingScreen()
    }
}