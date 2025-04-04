package com.bunbeauty.tiptoplive.features.subscription.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRote
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.subscription.presentation.Subscription
import com.bunbeauty.tiptoplive.features.subscription.presentation.SubscriptionViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val blurredBackground: Color
    @Composable
    get() = FakeLiveTheme.colors.background.copy(alpha = 0.7f)

data class SubscriptionItem(
    val id: String,
    val offerToken: String?,
    val name: String,
    val currentPrice: String,
    val previousPrice: String,
    val discountPercent: String,
    val isLifetime: Boolean,
    val isSelected: Boolean,
)

@Composable
fun SubscriptionScreen(
    navController: NavHostController,
    startCheckout: (PurchaseData) -> Unit
) {
    val viewModel: SubscriptionViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Subscription.Action ->
            viewModel.onAction(action)
        }
    }

    BackHandler {
        onAction(Subscription.Action.CloseClicked)
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Subscription.Event.NavigateBack -> {
                    navController.popBackStack()
                }

                is Subscription.Event.StartCheckout -> {
                    startCheckout(event.purchaseData)
                }

                is Subscription.Event.NavigateToPurchase -> {
                    navController.navigate(NavigationRote.SuccessfullyPurchased) {
                        popUpTo<NavigationRote.Preparation> {}
                    }
                }

                is Subscription.Event.NavigateToPurchaseFailed -> {
                    navController.navigate(NavigationRote.PurchaseFailed)
                }
            }
        }.launchIn(this)
    }

    SubscriptionContent(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun SubscriptionContent(
    state: Subscription.State,
    onAction: (Subscription.Action) -> Unit
) {
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bg_social_networks),
            contentScale = ContentScale.Crop,
            contentDescription = "Background"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FakeLiveTheme.colors.background.copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.subscription_unlimited_possibilities),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.titleLarge.bold,
                textAlign = TextAlign.Center
            )

            val shape = RoundedCornerShape(16.dp)
            Column(
                modifier = Modifier
                    .clip(shape)
                    .background(blurredBackground)
                    .padding(16.dp),
                verticalArrangement = spacedBy(12.dp)
            ) {
                FeatureItem(
                    emoji = "⏰",
                    title = stringResource(R.string.subscription_unlimited_time_title),
                    description = stringResource(R.string.subscription_unlimited_time_description),
                )
                FeatureItem(
                    emoji = "\uD83D\uDC69\u200D❤\uFE0F\u200D\uD83D\uDC68",
                    title = stringResource(R.string.subscription_1m_viewers_title),
                    description = stringResource(R.string.subscription_1m_viewers_description),
                )
                FeatureItem(
                    emoji = "\uD83D\uDCAB",
                    title = stringResource(R.string.subscription_ai_generated_title),
                    description = stringResource(R.string.subscription_ai_generated_description),
                )
                FeatureItem(
                    emoji = "\uD83D\uDC8E",
                    title = stringResource(R.string.subscription_other_features_title),
                    description = stringResource(R.string.subscription_other_features_description),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.padding(bottom = 72.dp),
                verticalArrangement = spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.timer?.let { timer ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = blurredBackground)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.subscription_offer_ends, timer),
                            color = FakeLiveTheme.colors.onBackground,
                            style = FakeLiveTheme.typography.bodySmall.bold,
                        )
                    }
                }

                state.subscriptions.forEach { subscriptionItem ->
                    SubscriptionItem(
                        subscriptionItem = subscriptionItem,
                        onAction = onAction
                    )
                }
            }
        }

        if (state.isCrossIconVisible) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(24.dp)
                    .clickableWithoutIndication {
                        onAction(Subscription.Action.CloseClicked)
                    },
                painter = painterResource(R.drawable.ic_close),
                tint = FakeLiveTheme.colors.onSurface,
                contentDescription = "Top icon"
            )
        }

        FakeLivePrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            text = stringResource(R.string.subscription_checkout),
            onClick = {
                onAction(Subscription.Action.CheckoutClick)
            }
        )
    }
}

@Composable
private fun FeatureItem(
    emoji: String,
    title: String,
    description: String,
) {
    Row {
        Text(
            text = emoji,
            style = FakeLiveTheme.typography.titleMedium
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = title,
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = description,
                color = FakeLiveTheme.colors.onSurfaceVariant,
                style = FakeLiveTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SubscriptionItem(
    subscriptionItem: SubscriptionItem,
    onAction: (Subscription.Action) -> Unit,
) {
    val borderColor = if (subscriptionItem.isSelected) {
        FakeLiveTheme.colors.interactive
    } else {
        Color.Transparent
    }
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(blurredBackground)
            .border(
                width = 1.5.dp,
                color = borderColor,
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
                        color = FakeLiveTheme.colors.onBackground,
                        style = FakeLiveTheme.typography.titleMedium,
                    )
                    if (subscriptionItem.isLifetime) {
                        Label(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.subscription_use_forever)
                        )
                    } else {
                        Label(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.subscription_save, subscriptionItem.discountPercent)
                        )
                    }
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
                selectedColor = FakeLiveTheme.colors.interactive,
                unselectedColor = FakeLiveTheme.colors.onSurfaceVariant,
            ),
            onClick = {},
        )
    }
}

@Composable
private fun Label(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = FakeLiveTheme.colors.interactive.copy(alpha = 0.1f))
            .padding(
                horizontal = 6.dp,
                vertical = 2.dp
            )
    ) {
        Text(
            text = text,
            color = FakeLiveTheme.colors.interactive,
            style = FakeLiveTheme.typography.bodySmall,
        )
    }
}

@LocalePreview
@Composable
private fun SubscriptionScreenPreview() {
    FakeLiveTheme {
        SubscriptionContent(
            state = Subscription.State(
                subscriptions = listOf(
                    SubscriptionItem(
                        id = "1",
                        offerToken = "",
                        name = "Weekly",
                        currentPrice = "$2,99/week",
                        previousPrice = "$10,99/month",
                        discountPercent = "Save 70%",
                        isLifetime = false,
                        isSelected = true
                    ),
                    SubscriptionItem(
                        id = "2",
                        offerToken = null,
                        name = "Lifetime",
                        currentPrice = "$6,99",
                        previousPrice = "$20,99/month",
                        discountPercent = "$20,99/month",
                        isLifetime = true,
                        isSelected = false
                    )
                ),
                timer = "12:00:00",
                isCrossIconVisible = true
            ),
            onAction = {}
        )
    }
}