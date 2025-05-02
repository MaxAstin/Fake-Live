package com.bunbeauty.tiptoplive.common.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

@Composable
fun instagramBrush(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(
            FakeLiveTheme.colors.instagram.logo1,
            FakeLiveTheme.colors.instagram.logo2,
            FakeLiveTheme.colors.instagram.logo3
        )
    )
}