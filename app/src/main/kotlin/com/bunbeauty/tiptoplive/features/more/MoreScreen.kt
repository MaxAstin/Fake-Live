package com.bunbeauty.tiptoplive.features.more

import androidx.activity.compose.LocalActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.rippleClickable
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.instagramBrush
import com.bunbeauty.tiptoplive.common.util.openMarketListing
import com.bunbeauty.tiptoplive.common.util.openSharing
import com.bunbeauty.tiptoplive.common.util.playMarketLink
import com.bunbeauty.tiptoplive.features.more.presentation.More
import com.bunbeauty.tiptoplive.features.more.presentation.MoreViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MoreScreen(navController: NavHostController) {
    val viewModel: MoreViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: More.Action ->
            viewModel.onAction(action)
        }
    }
    val activity = LocalActivity.current
    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                More.Event.NavigateToPremiumDetails -> {
                    navController.navigate(NavigationRoute.PremiumDetails)
                }

                More.Event.OpenSharing -> {
                    activity?.apply {
                        openSharing(
                            text = getString(
                                R.string.sharing_text,
                                getString(R.string.app_name),
                                playMarketLink
                            )
                        )
                    }
                }

                More.Event.NavigateToFeedback -> {
                    navController.navigate(NavigationRoute.Feedback)
                }

                More.Event.NavigateToFeedbackSuccess -> {
                    navController.navigate(NavigationRoute.FeedbackSuccess)
                }

                More.Event.OpenMarketListing -> {
                    activity?.openMarketListing()
                }
            }
        }.launchIn(this)
    }

    MoreContent(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun MoreContent(
    state: More.State,
    onAction: (More.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveTheme.colors.background)
    ) {
        Option(
            iconRes = R.drawable.ic_infinity,
            text = stringResource(R.string.common_premium),
            onClick = {
                onAction(More.Action.PremiumClick)
            },
            extraContent = {
                PremiumChip(premium = state.premium)
            }
        )
        Option(
            iconRes = R.drawable.ic_share,
            text = stringResource(R.string.more_share),
            onClick = {
                onAction(More.Action.ShareClick)
            }
        )
        Option(
            iconRes = R.drawable.ic_feedback,
            text = stringResource(R.string.more_feedback),
            onClick = {
                onAction(More.Action.FeedbackClick)
            }
        )
        Option(
            iconRes = R.drawable.ic_star,
            text = stringResource(R.string.more_rate_us),
            onClick = {
                onAction(More.Action.RateUsClick)
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
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
            contentDescription = "arrow icon",
            tint = FakeLiveTheme.colors.onBackgroundVariant,
        )
    }
}

@Composable
private fun PremiumChip(
    premium: More.Premium,
    modifier: Modifier = Modifier
) {
    when (premium) {
        More.Premium.Loading -> {}
        More.Premium.Active -> {
            Text(
                modifier = modifier
                    .background(
                        brush = instagramBrush(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                text = stringResource(R.string.more_premium_active),
                style = FakeLiveTheme.typography.bodyMedium,
                color = FakeLiveTheme.colors.onSurface
            )
        }

        is More.Premium.Discount -> {
            Row(
                modifier = modifier
                    .background(
                        color = FakeLiveTheme.colors.negative,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(start = 6.dp, end = 12.dp)
                    .padding(vertical = 6.dp),
                horizontalArrangement = spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .background(
                            color = FakeLiveTheme.colors.background,
                            shape = CircleShape
                        )
                        .size(20.dp)
                        .padding(4.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_discount),
                    contentDescription = "discount icon",
                    tint = FakeLiveTheme.colors.negative,
                )
                Text(
                    text = premium.timer,
                    style = FakeLiveTheme.typography.bodyMedium,
                    color = FakeLiveTheme.colors.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoreScreenPreview() {
    FakeLiveTheme {
        PremiumChip(More.Premium.Active)
    }
}