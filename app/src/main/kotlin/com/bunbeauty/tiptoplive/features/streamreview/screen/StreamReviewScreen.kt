package com.bunbeauty.tiptoplive.features.streamreview.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveScaffold
import com.bunbeauty.tiptoplive.common.ui.components.TopBarWithCloseIcon
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveSecondaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.common.ui.util.AppearWithDelay

@Composable
fun StreamReviewScreen(navController: NavHostController) {
    FakeLiveScaffold(
        topBar = {
            TopBarWithCloseIcon(onCloseIconClick = {})
        },
        bottomButton = {
            AppearWithDelay {
                FakeLiveSecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.stream_do_not_ask_again),
                    onClick = {}
                )
            }
        }
    ) {
        Column {
            Spacer(modifier = Modifier.weight(2f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.stream_review_how_was_it),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.titleLarge.bold,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.padding(top = 32.dp),
                horizontalArrangement = spacedBy(8.dp)
            ) {
                EmojiCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.stream_review_good),
                    imageId = R.drawable.img_face_with_heart_eyes,
                    caption = stringResource(R.string.stream_review_rate)
                )
                EmojiCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.stream_review_not_good),
                    imageId = R.drawable.img_face_angry,
                    caption = stringResource(R.string.stream_review_give_feedback)
                )
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Composable
private fun EmojiCard(
    title: String,
    @DrawableRes imageId: Int,
    caption: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = FakeLiveTheme.colors.background
        ),
        border = BorderStroke(
            1.dp,
            color = FakeLiveTheme.colors.borderVariant
        ),
        onClick = {}
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = title,
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.titleSmall,
            )
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = imageId),
                contentDescription = "emoji"
            )
            Text(
                text = caption,
                color = FakeLiveTheme.colors.onBackgroundVariant,
                style = FakeLiveTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
private fun StreamReviewScreenPreview() {
    FakeLiveTheme {
        StreamReviewScreen(rememberNavController())
    }
}