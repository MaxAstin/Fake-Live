package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold

@Composable
fun PremiumBanner(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFF737EFA)
            )
            .clickableWithoutIndication(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterVertically),
            verticalArrangement = spacedBy(4.dp)
        ) {
            Text(
                text = "Premium",
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.titleMedium.bold
            )

            // TODO use real data
            val text = buildAnnotatedString {
                append("Special offer: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("10:00:00")
                }
            }
            Text(
                text = text,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
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

@Preview
@Composable
private fun PremiumBannerPreview() {
    FakeLiveTheme {
        PremiumBanner(onClick = {})
    }
}