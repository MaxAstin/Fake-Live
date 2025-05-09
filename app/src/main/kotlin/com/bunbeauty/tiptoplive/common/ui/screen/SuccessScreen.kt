package com.bunbeauty.tiptoplive.common.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.components.CloseIcon
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun SuccessScreen(
    title: String,
    description: String,
    mainButtonText: String,
    modifier: Modifier = Modifier,
    onMainButtonClick: () -> Unit = {},
    onCloseIconOnClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FakeLiveTheme.colors.background,
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                CloseIcon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp),
                    onClick = onCloseIconOnClick
                )
            }
        },
        floatingActionButton = {
            FakeLivePrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = mainButtonText,
                onClick = onMainButtonClick
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .background(
                        color = FakeLiveTheme.colors.positive,
                        shape = CircleShape
                    )
                    .padding(32.dp)
                    .size(64.dp),
                painter = painterResource(R.drawable.ic_check),
                tint = FakeLiveTheme.colors.onSurface,
                contentDescription = "Success check"
            )
            Text(
                modifier = Modifier.padding(top = 64.dp),
                text = title,
                style = FakeLiveTheme.typography.titleMedium,
                color = FakeLiveTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = description,
                style = FakeLiveTheme.typography.bodyMedium,
                color = FakeLiveTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun SuccessScreenPreview() {
    FakeLiveTheme {
        SuccessScreen(
            title = "Title",
            description = "Description. It's description. Simple description. Long description. My description. Cool description.",
            mainButtonText = "Action",
        )
    }
}