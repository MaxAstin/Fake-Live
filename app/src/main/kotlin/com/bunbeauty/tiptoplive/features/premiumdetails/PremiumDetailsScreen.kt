package com.bunbeauty.tiptoplive.features.premiumdetails

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.bunbeauty.tiptoplive.common.ui.util.meshGradient
import com.bunbeauty.tiptoplive.features.subscription.presentation.Subscription
import com.bunbeauty.tiptoplive.features.subscription.presentation.SubscriptionViewModel
import com.bunbeauty.tiptoplive.features.subscription.view.SubscriptionItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

private val surfaceColor = Color(0xFF262036)
private val highlightColor = Color(0x33FFFFFF)

private val logo1 = Color(0xFF9C20FA)
private val logo2 = Color(0xFFFF207E)
private val logo3 = Color(0xFFFFBF01)

@Composable
fun PremiumDetailsScreen(
    navController: NavHostController
) {
    val viewModel: SubscriptionViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Subscription.Action ->
            viewModel.onAction(action)
        }
    }

    PremiumDetailsContent(
        navController = navController,
        state = state,
        onAction = onAction
    )
}

@Composable
fun PremiumDetailsContent(
    navController: NavHostController,
    state: Subscription.State,
    onAction: (Subscription.Action) -> Unit
) {
    var bottomHeightPx by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.premiumGradient(),
        containerColor = Color.Unspecified,
        contentColor = Color.Unspecified,
        floatingActionButton = {
            if (state.freePlan.isCurrent) {
                BottomBlock(
                    modifier = Modifier.onSizeChanged { size ->
                        bottomHeightPx = size.height
                    },
                    state = state,
                    onAction = onAction
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            val bottomHeightDp = with(LocalDensity.current) { bottomHeightPx.toDp() }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = spacedBy(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    val titleTextId = if (state.isPremiumSelected) {
                        R.string.preparation_premium
                    } else {
                        R.string.subscription_free
                    }
                    Text(
                        text = stringResource(titleTextId),
                        color = FakeLiveTheme.colors.onSurface,
                        style = FakeLiveTheme.typography.titleLarge.bold,
                        textAlign = TextAlign.Center
                    )

                    val animatedAlpha by animateFloatAsState(
                        targetValue = if (state.selectedPlan.isCurrent) 1f else 0f,
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

                val scrollState = rememberScrollState()
                val scope = rememberCoroutineScope()

                SegmentedControl(
                    backgroundColor = surfaceColor,
                    selectedContainerColor = highlightColor,
                ) {
                    persistentListOf(
                        stringResource(R.string.subscription_free),
                        stringResource(R.string.preparation_premium)
                    ).forEachIndexed { index, text ->
                        SegmentedControlButton(
                            onClick = {
                                scope.launch {
                                    scrollState.animateScrollTo(0)
                                }
                                onAction(
                                    Subscription.Action.SelectPlan(index = index)
                                )
                            },
                            text = text,
                            selected = index == state.selectedIndex,
                            selectedColor = FakeLiveTheme.colors.onSurface,
                            unselectedColor = FakeLiveTheme.colors.onSurfaceVariant,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(bottom = 16.dp + bottomHeightDp + 16.dp),
                    verticalArrangement = spacedBy(8.dp)
                ) {
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
                            description = if (state.isPremiumSelected) {
                                stringResource(R.string.subscription_unlimited_live_time_description)
                            } else {
                                stringResource(R.string.subscription_limited_live_time_description)
                            },
                            label = if (state.isPremiumSelected) {
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
                            description = if (state.isPremiumSelected) {
                                stringResource(R.string.subscription_1m_viewers_description)
                            } else {
                                stringResource(R.string.subscription_limited_viewers_description)
                            },
                            label = if (state.isPremiumSelected) {
                                stringResource(R.string.subscription_1m_label)
                            } else {
                                stringResource(R.string.subscription_100_200_label)
                            }
                        )
                    }
                    val animatedAlpha by animateFloatAsState(
                        targetValue = if (state.isPremiumSelected) 1f else 0f,
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
                            title = stringResource(R.string.subscription_even_more_title),
                            description = stringResource(R.string.subscription_even_more_description)
                        )
                    }
                }
            }

            val crossIconAlpha by animateFloatAsState(
                targetValue = if (state.isCrossIconVisible) 1f else 0f,
                label = "CrossIconAlpha"
            )
            Icon(
                modifier = Modifier
                    .alpha(crossIconAlpha)
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(24.dp)
                    .clickableWithoutIndication {
                        navController.popBackStack()
                    },
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                tint = FakeLiveTheme.colors.onSurfaceVariant,
                contentDescription = "Top icon"
            )
        }
    }
}

@Composable
private fun BottomBlock(
    modifier: Modifier = Modifier,
    state: Subscription.State,
    onAction: (Subscription.Action) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = surfaceColor.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            state.timer?.let { timer ->
                Text(
                    text = "\uD83D\uDD25   ${stringResource(R.string.subscription_offer_ends, timer)}   \uD83D\uDD25",
                    color = FakeLiveTheme.colors.onSurface,
                    style = FakeLiveTheme.typography.bodySmall.bold,
                )
            }
        }
        state.selectedPlan.subscriptions.forEach { subscriptionItem ->
            SubscriptionItem(
                subscriptionItem = subscriptionItem,
                onAction = onAction
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(highlightColor)
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            FakeLivePrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (state.isPremiumSelected) {
                    stringResource(R.string.subscription_checkout)
                } else {
                    stringResource(R.string.subscription_get_premium)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FakeLiveTheme.colors.background,
                    contentColor = FakeLiveTheme.colors.onBackground
                ),
                contentPadding = PaddingValues(12.dp),
                onClick = {
                    if (state.isFreeSelected) {
                        onAction(
                            Subscription.Action.SelectPlan(index = 1)
                        )
                    } else {
                        onAction(Subscription.Action.CheckoutClick)
                    }
                }
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
            .padding(12.dp),
        verticalArrangement = spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(bottom = 4.dp)
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
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color = highlightColor)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .animateContentSize(),
                    text = label,
                    color = FakeLiveTheme.colors.onSurfaceVariant,
                    style = FakeLiveTheme.typography.bodyMedium.bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SubscriptionItem(
    subscriptionItem: SubscriptionItem,
    onAction: (Subscription.Action) -> Unit,
) {
    val borderColor = if (subscriptionItem.isSelected) {
        Brush.horizontalGradient(
            colors = listOf(logo1, logo2, logo3)
        )
    } else {
        SolidColor(Color.Transparent)
    }
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(surfaceColor)
            .border(
                width = 1.5.dp,
                brush = borderColor,
                shape = shape
            )
            .clickableWithoutIndication {
                onAction(Subscription.Action.SubscriptionClick(subscriptionItem.id))
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = subscriptionItem.name,
                color = FakeLiveTheme.colors.onBackgroundVariant,
                style = FakeLiveTheme.typography.bodyMedium.bold,
            )
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = subscriptionItem.currentPrice,
                        color = FakeLiveTheme.colors.onSurface,
                        style = FakeLiveTheme.typography.titleMedium,
                    )
                    val labelText = if (subscriptionItem.isLifetime) {
                        stringResource(R.string.subscription_use_forever)
                    } else {
                        stringResource(R.string.subscription_save, subscriptionItem.discountPercent)
                    }
                    Label(
                        modifier = Modifier.padding(start = 8.dp),
                        text = labelText,
                        isSelected = subscriptionItem.isSelected
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = subscriptionItem.previousPrice,
                    color = FakeLiveTheme.colors.onBackgroundVariant,
                    style = FakeLiveTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }
        }
        RadioButton(
            modifier = Modifier.align(Alignment.TopEnd),
            selected = subscriptionItem.isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = logo3.copy(alpha = 0.8f),
                unselectedColor = FakeLiveTheme.colors.onSurfaceVariant,
            ),
            onClick = {},
        )
    }
}

@Composable
private fun Label(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val labelBackgroundColor by animateColorAsState(
        if (isSelected) {
            logo3
        } else {
            FakeLiveTheme.colors.onSurfaceVariant
        },
        label = "LabelBackground"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(labelBackgroundColor)
            .padding(
                horizontal = 6.dp,
                vertical = 2.dp
            )
    ) {
        val textColor by animateColorAsState(
            if (isSelected) {
                surfaceColor
            } else {
                Color(0xFF201F23)
            },
            label = "LabelTextColor"
        )
        Text(
            text = text,
            color = textColor,
            style = FakeLiveTheme.typography.bodySmall
        )
    }
}

@Composable
private fun Modifier.premiumGradient(): Modifier {
    val topBlack = Color(0xFF000000)
    val middleBlack = Color(0xFF0F061F)
    return meshGradient(
        points = listOf(
            listOf(
                Offset(0f, 0f) to topBlack,
                Offset(0.5f, 0f) to topBlack,
                Offset(1f, 0f) to topBlack
            ),
            listOf(
                Offset(0f, 0.2f) to middleBlack,
                Offset(0.5f, 0.2f) to middleBlack,
                Offset(1f, 0.2f) to middleBlack
            ),
            listOf(
                Offset(0f, 1f) to logo1,
                Offset(0.5f, 1f) to logo2,
                Offset(1f, 1f) to logo3,
            )
        ),
        resolutionX = 16,
        resolutionY = 16,
    )
}

@LocalePreview
@Composable
private fun PremiumDetailsScreenPreview() {
    FakeLiveTheme {
        PremiumDetailsScreen(navController = rememberNavController())
    }
}