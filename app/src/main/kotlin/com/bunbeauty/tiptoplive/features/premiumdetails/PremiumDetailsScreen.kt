package com.bunbeauty.tiptoplive.features.premiumdetails

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold

@Composable
fun PremiumDetailsScreen(
    navController: NavHostController
) {
    val backgroundTop = Color(0xFF060606)
    val backgroundBottom = Color(0xFF39205D)
    val brush = Brush.verticalGradient(
        colors = listOf(
            backgroundTop,
            backgroundTop,
            backgroundBottom
        )
    )
    Box(modifier = Modifier.background(brush)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.preparation_premium),
                    color = FakeLiveTheme.colors.onSurface,
                    style = FakeLiveTheme.typography.titleLarge.bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color = Color(0x1AFFFFFF))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    text = stringResource(R.string.subscription_current),
                    color = FakeLiveTheme.colors.onSurfaceVariant,
                    style = FakeLiveTheme.typography.bodySmall
                )
            }

            Column(verticalArrangement = spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = spacedBy(8.dp)
                ) {
                    FeatureTile(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        iconId = R.drawable.ic_time,
                        title = stringResource(R.string.subscription_live_time_title),
                        description = stringResource(R.string.subscription_unlimited_live_time_description),
                        label = stringResource(R.string.subscription_unlimited_label)
                    )
                    FeatureTile(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        iconId = R.drawable.ic_view,
                        title = stringResource(R.string.subscription_viewers_title),
                        description = stringResource(R.string.subscription_1m_viewers_description),
                        label = stringResource(R.string.subscription_1m_label)
                    )
                }
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = spacedBy(8.dp)
                ) {
                    FeatureTile(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        iconId = R.drawable.ic_sparks,
                        title = stringResource(R.string.subscription_ai_features_title),
                        description = stringResource(R.string.subscription_ai_features_description),
                    )
                    FeatureTile(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        iconId = R.drawable.ic_crown,
                        title = stringResource(R.string.subscription_other_premium_features_title),
                        description = stringResource(R.string.subscription_other_premium_features_description)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickableWithoutIndication {
                    navController.popBackStack()
                },
            imageVector = ImageVector.vectorResource(R.drawable.ic_close),
            tint = FakeLiveTheme.colors.onSurface,
            contentDescription = "Top icon"
        )
    }
}

@Composable
private fun FeatureTile(
    @DrawableRes iconId: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    val background = Color(0x66332D3B)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .padding(16.dp),
        verticalArrangement = spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconBackground = Color(0x1AFFFFFF)
        Icon(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clip(CircleShape)
                .background(color = iconBackground)
                .padding(12.dp)
                .size(24.dp),
            imageVector = ImageVector.vectorResource(iconId),
            contentDescription = "Infinity",
            tint = FakeLiveTheme.colors.onSurface,
        )
        Text(
            text = title,
            color = FakeLiveTheme.colors.onSurface,
            style = FakeLiveTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = description,
            color = FakeLiveTheme.colors.onSurfaceVariant,
            style = FakeLiveTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        label?.let {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color = iconBackground)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    text = label,
                    color = FakeLiveTheme.colors.onSurfaceVariant,
                    style = FakeLiveTheme.typography.bodyMedium.bold
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun PremiumDetailsScreenPreview() {
    FakeLiveTheme {
        PremiumDetailsScreen(navController = rememberNavController())
    }
}