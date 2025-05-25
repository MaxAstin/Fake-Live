package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold

@Composable
fun PremiumBanner(
    timer: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FakeLiveTheme.colors.premium)
            .clickableWithoutIndication(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.common_premium),
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.titleMedium.bold
            )

            val text = buildAnnotatedString {
                append(stringResource(R.string.preparation_special_offer))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" ")
                    append(timer)
                }
            }
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = text,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.preparation_upgrade_now),
                    color = FakeLiveTheme.colors.onSurface,
                    style = FakeLiveTheme.typography.titleSmall,
                )
                Icon(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    tint = FakeLiveTheme.colors.onSurface,
                    contentDescription = "arrow"
                )
            }
        }
        Image(
            modifier = Modifier
                .width(128.dp)
                .height(72.dp)
                .align(Alignment.Bottom),
            painter = painterResource(id = R.drawable.img_cool_guys),
            contentDescription = "image"
        )
    }
}

@LocalePreview
@Composable
private fun PremiumBannerPreview() {
    FakeLiveTheme {
        PremiumBanner(
            timer = "12:00:00",
            onClick = {}
        )
    }
}