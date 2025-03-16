package com.bunbeauty.tiptoplive.common.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun CloseIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = FakeLiveTheme.colors.onBackground
) {
    Icon(
        modifier = modifier
            .size(24.dp)
            .clickableWithoutIndication(onClick = onClick),
        painter = painterResource(R.drawable.ic_close),
        tint = tint,
        contentDescription = "Top icon"
    )
}