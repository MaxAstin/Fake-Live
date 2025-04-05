package com.bunbeauty.tiptoplive.features.premiumdetails

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.bunbeauty.tiptoplive.common.ui.components.SegmentedControl
import com.bunbeauty.tiptoplive.common.ui.components.SegmentedControlButton
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import kotlinx.collections.immutable.persistentListOf

private val surfaceColor = Color(0x66332D3B)
private val highlightColor = Color(0x1AFFFFFF)

// TODO
// Add view model and state
// Change descriptions for FREE
// Add subscriptions cards
// Move logic from subscriptions

@Composable
fun PremiumDetailsScreen(
    navController: NavHostController
) {
    var selectedSegmentIndex by remember { mutableIntStateOf(1) }
    val isPremium = remember(selectedSegmentIndex) { selectedSegmentIndex == 1 }
    val isCurrentPremium = false

    val backgroundTop = Color(0xFF060606)
    val backgroundBottom = Color(0xFF39205D)
    val brush = Brush.verticalGradient(
        colors = listOf(
            backgroundTop,
            backgroundTop,
            backgroundBottom
        )
    )

    Scaffold(
        modifier = Modifier.background(brush),
        containerColor = Color.Unspecified,
        contentColor = Color.Unspecified,
        floatingActionButton = {
            if (!isCurrentPremium) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(highlightColor)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    FakeLivePrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = if (isPremium) {
                            "Continue to checkout"
                        } else {
                            "Get Premium"
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FakeLiveTheme.colors.background,
                            contentColor = Color(0xFF1E053B)
                        ),
                        contentPadding = PaddingValues(12.dp),
                        onClick = {
                            if (selectedSegmentIndex == 0) {
                                selectedSegmentIndex = 1
                            }
                        }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = spacedBy(32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    val textId = if (isPremium) {
                        R.string.preparation_premium
                    } else {
                        R.string.subscription_free
                    }
                    Text(
                        text = stringResource(textId),
                        color = FakeLiveTheme.colors.onSurface,
                        style = FakeLiveTheme.typography.titleLarge.bold,
                        textAlign = TextAlign.Center
                    )

                    val animatedAlpha by animateFloatAsState(
                        targetValue = if (isPremium && isCurrentPremium || !isPremium && !isCurrentPremium) 1f else 0f,
                        label = "CurrentAlpha"
                    )
                    Text(
                        modifier = Modifier
                            .alpha(animatedAlpha)
                            .clip(RoundedCornerShape(6.dp))
                            .background(color = highlightColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        text = stringResource(R.string.subscription_current),
                        color = FakeLiveTheme.colors.onSurfaceVariant,
                        style = FakeLiveTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                SegmentedControl(
                    backgroundColor = surfaceColor,
                    selectedContainerColor = highlightColor,
                ) {
                    persistentListOf(
                        stringResource(R.string.subscription_free),
                        stringResource(R.string.preparation_premium)
                    ).forEachIndexed { index, text ->
                        SegmentedControlButton(
                            onClick = { selectedSegmentIndex = index },
                            text = text,
                            selected = index == selectedSegmentIndex,
                            selectedColor = FakeLiveTheme.colors.onSurface,
                            unselectedColor = FakeLiveTheme.colors.onSurfaceVariant,
                        )
                    }
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
                            label = if (isPremium) {
                                stringResource(R.string.subscription_unlimited_label)
                            } else {
                                stringResource(R.string.subscription_60s_label)
                            }
                        )
                        FeatureTile(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            iconId = R.drawable.ic_view,
                            title = stringResource(R.string.subscription_viewers_title),
                            description = stringResource(R.string.subscription_1m_viewers_description),
                            label = if (isPremium) {
                                stringResource(R.string.subscription_1m_label)
                            } else {
                                stringResource(R.string.subscription_100_200_label)
                            }
                        )
                    }
                    val animatedAlpha by animateFloatAsState(
                        targetValue = if (isPremium) 1f else 0f,
                        label = "FeaturesAlpha"
                    )
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .alpha(animatedAlpha),
                        horizontalArrangement = spacedBy(8.dp)
                    ) {
                        FeatureTile(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            iconId = R.drawable.ic_sparks,
                            title = stringResource(R.string.subscription_ai_features_title),
                            description = stringResource(R.string.subscription_ai_features_description)
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
                tint = FakeLiveTheme.colors.onSurface.copy(alpha = 0.6f),
                contentDescription = "Top icon"
            )
        }
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
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(surfaceColor)
            .padding(16.dp),
        verticalArrangement = spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clip(CircleShape)
                .background(color = highlightColor)
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
                        .background(color = highlightColor)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .animateContentSize(),
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