package com.bunbeauty.tiptoplive.common.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun TopBarWithCloseIcon(
    onCloseIconClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        CloseIcon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp),
            onClick = onCloseIconClick
        )
    }
}

@Preview
@Composable
private fun TopBarWithCloseIconPreview() {
    FakeLiveTheme {
        TopBarWithCloseIcon(onCloseIconClick = {})
    }
}