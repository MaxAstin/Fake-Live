package com.bunbeauty.tiptoplive.features.more

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.rippleClickable
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.instagramBrush

@Composable
fun MoreScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveTheme.colors.background)
    ) {
        Option(
            iconRes = R.drawable.ic_infinity,
            text = "Premium",
            onClick = {

            },
            extraContent = {
                Text(
                    modifier = Modifier
                        .background(
                            brush = instagramBrush(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                    text = "Active",
                    style = FakeLiveTheme.typography.bodyMedium,
                    color = FakeLiveTheme.colors.onSurface
                )
            }
        )
        Option(
            iconRes = R.drawable.ic_share,
            text = "Share",
            onClick = {

            }
        )
        Option(
            iconRes = R.drawable.ic_feedback,
            text = "Feedback",
            onClick = {

            }
        )
        Option(
            iconRes = R.drawable.ic_star,
            text = "Rate us",
            onClick = {

            }
        )
    }
}

@Composable
private fun Option(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    extraContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .rippleClickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        horizontalArrangement = spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = "start icon",
            tint = FakeLiveTheme.colors.onBackground,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = FakeLiveTheme.typography.bodyMedium,
            color = FakeLiveTheme.colors.onBackground
        )
        extraContent()
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = "arrow right icon",
            tint = FakeLiveTheme.colors.onBackgroundVariant,
        )
    }
}

@Preview
@Composable
fun MoreScreenPreview() {
    FakeLiveTheme {
        MoreScreen()
    }
}