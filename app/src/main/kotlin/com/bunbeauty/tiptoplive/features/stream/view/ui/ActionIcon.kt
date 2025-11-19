package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun ActionIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int,
    contentDescription: String,
) {
    Icon(
        modifier = modifier.size(28.dp),
        imageVector = ImageVector.vectorResource(iconResId),
        contentDescription = contentDescription,
        tint = FakeLiveTheme.colors.icon,
    )
}

@Preview
@Composable
fun ActionIconPreview() {
    FakeLiveTheme {
        ActionIcon(
            iconResId = R.drawable.ic_camera,
            contentDescription = "",
        )
    }
}