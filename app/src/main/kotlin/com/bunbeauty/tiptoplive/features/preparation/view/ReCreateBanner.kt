package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold

private const val RECREATE_APP_URL =
    "https://play.google.com/store/apps/details?id=com.recreate.photo&utm_source=live_app"

@Composable
fun ReCreateBanner(
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(12.dp))
            .background(FakeLiveTheme.colors.premium)
            .clickableWithoutIndication(onClick = {
                uriHandler.openUri(RECREATE_APP_URL)
            })
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(
                    R.string.preparation_recreate_title,
                    stringResource(R.string.recreate_app_name)
                ),
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.titleMedium.bold
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(R.string.preparation_recreate_description),
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(FakeLiveTheme.colors.onSurface)
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.preparation_recreate_try_it_now),
                    color = FakeLiveTheme.colors.surface,
                    style = FakeLiveTheme.typography.titleSmall,
                )
                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    tint = FakeLiveTheme.colors.surface,
                    contentDescription = "arrow"
                )
            }
        }
        Image(
            modifier = Modifier
                .width(132.dp)
                .fillMaxHeight(),
            painter = painterResource(id = R.drawable.img_recreate_banner),
            contentScale = ContentScale.Crop,
            contentDescription = "ReCreate app"
        )
    }
}

@LocalePreview
@Composable
private fun ReCreateBannerPreview() {
    FakeLiveTheme {
        ReCreateBanner()
    }
}
