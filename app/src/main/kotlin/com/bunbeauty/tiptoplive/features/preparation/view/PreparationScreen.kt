package com.bunbeauty.tiptoplive.features.preparation.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.CachedImage
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveSwitch
import com.bunbeauty.tiptoplive.common.ui.components.FakeLiveTextField
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveGradientButton
import com.bunbeauty.tiptoplive.common.ui.noEffectClickable
import com.bunbeauty.tiptoplive.common.ui.rippleClickable
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.instagramBrush
import com.bunbeauty.tiptoplive.features.preparation.RequestNotificationsPermission
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation
import com.bunbeauty.tiptoplive.features.preparation.presentation.Preparation.ViewerCountItem
import com.bunbeauty.tiptoplive.features.preparation.presentation.PreparationViewModel
import com.bunbeauty.tiptoplive.features.stream.presentation.TIME_LIMIT_FOR_FREE_VERSION
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val IMAGE = "image/*"

@Composable
fun PreparationScreen(
    navController: NavHostController,
    onStartStreamClick: () -> Unit,
    onPositiveFeedbackClick: () -> Unit
) {
    val viewModel: PreparationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Preparation.Action ->
            viewModel.onAction(action)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        val uriParam = uri?.toString() ?: return@rememberLauncherForActivityResult
        navController.navigate(
            NavigationRoute.CropImage(uri = uriParam)
        )
    }

    RequestNotificationsPermission {
        viewModel.onAction(Preparation.Action.SetupNotification)
    }
    LaunchedEffect(Unit) {
        viewModel.onAction(Preparation.Action.StartScreen)
    }
    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Preparation.Event.OpenStream -> {
                    onStartStreamClick()
                }

                Preparation.Event.HandlePositiveFeedbackClick -> {
                    onPositiveFeedbackClick()
                }

                Preparation.Event.HandleAvatarClick -> {
                    galleryLauncher.launch(IMAGE)
                }

                Preparation.Event.NavigateToPremiumDetails -> {
                    navController.navigate(NavigationRoute.PremiumDetails)
                }
            }
        }.launchIn(this)
    }

    PreparationContent(
        state = state,
        onAction = onAction,
    )

    if (state.showStreamDurationLimitsDialog == true) {
        StreamDurationLimitsDialog(onAction = onAction)
    }
}

@Composable
private fun PreparationContent(
    state: Preparation.State,
    onAction: (Preparation.Action) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveTheme.colors.background)
            .padding(16.dp)
    ) {
        Premium(
            modifier = Modifier.align(Alignment.TopEnd),
            premiumStatus = state.premiumStatus,
            onAction = onAction
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = spacedBy(16.dp)
        ) {
            AvatarBlock(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                image = state.image,
                onAction = onAction
            )
            UsernameBlock(
                modifier = Modifier.fillMaxWidth(),
                username = state.username,
                onAction = onAction
            )
            ViewerCountBlock(
                modifier = Modifier.fillMaxWidth(),
                viewerCount = state.viewerCount,
                viewerCountList = state.viewerCountList,
                onAction = onAction
            )
            RecordingSwitcher(
                modifier = Modifier.fillMaxWidth(),
                isRecording = state.isRecording,
                onAction = onAction
            )
        }

        FakeLiveGradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            brush = instagramBrush(),
            shape = RoundedCornerShape(6.dp),
            onClick = {
                onAction(Preparation.Action.StartStreamClick)
            }
        ) {
            val startLiveText = stringResource(R.string.preparation_start_live)
            val secondsText = stringResource(R.string.preparation_seconds, TIME_LIMIT_FOR_FREE_VERSION)
            val text = remember(state.premiumStatus) {
                if (state.premiumStatus is Preparation.PremiumStatus.Free) {
                    "$startLiveText $secondsText"
                } else {
                    startLiveText
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(12.dp),
                text = text,
                color = FakeLiveTheme.colors.onSurface,
                style = FakeLiveTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun Premium(
    premiumStatus: Preparation.PremiumStatus,
    onAction: (Preparation.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    when (premiumStatus) {
        Preparation.PremiumStatus.Loading -> Unit
        is Preparation.PremiumStatus.Free -> {
            PremiumBanner(
                modifier = modifier,
                timer = premiumStatus.offerTimer,
                onClick = {
                    onAction(Preparation.Action.PremiumClick)
                }
            )
        }

        Preparation.PremiumStatus.Active -> {
            Row(
                modifier = modifier
                    .background(
                        color = FakeLiveTheme.colors.premium,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
                    .clickableWithoutIndication {
                        onAction(Preparation.Action.PremiumClick)
                    },
                horizontalArrangement = spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.common_premium),
                    color = FakeLiveTheme.colors.onSurface,
                    style = FakeLiveTheme.typography.titleSmall,
                )
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_infinity),
                    contentDescription = "Infinity",
                    tint = FakeLiveTheme.colors.onSurface,
                )
            }
        }
    }
}

@Composable
private fun AvatarBlock(
    image: ImageSource<Any>,
    onAction: (Preparation.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.noEffectClickable {
            onAction(Preparation.Action.AvatarClick)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CachedImage(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            imageSource = image,
            cacheKey = image.data.toString(),
            contentDescription = "Avatar",
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.preparation_edit_photo),
            color = FakeLiveTheme.colors.interactive,
            style = FakeLiveTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun UsernameBlock(
    username: String,
    onAction: (Preparation.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.preparation_username),
            color = FakeLiveTheme.colors.onSurfaceVariant,
            style = FakeLiveTheme.typography.bodyMedium,
        )
        FakeLiveTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            hint = stringResource(R.string.preparation_username_hint),
            readOnly = false,
            onValueChange = { username ->
                onAction(Preparation.Action.UsernameUpdate(username = username))
            },
        )
    }
}

@Composable
private fun ViewerCountBlock(
    viewerCount: ViewerCount,
    viewerCountList: ImmutableList<ViewerCountItem>,
    onAction: (Preparation.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.preparation_viewer_count),
            color = FakeLiveTheme.colors.onSurfaceVariant,
            style = FakeLiveTheme.typography.bodyMedium,
        )
        Box {
            var menuExpanded by remember {
                mutableStateOf(false)
            }
            FakeLiveTextField(
                modifier = Modifier.rippleClickable {
                    menuExpanded = true
                },
                value = viewerCount.text,
                hint = "",
                readOnly = true,
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                        contentDescription = "Arrow",
                        tint = FakeLiveTheme.colors.iconVariant,
                    )
                }
            )
            ViewersDropdownMenu(
                expanded = menuExpanded,
                viewerCountList = viewerCountList,
                onDismissRequest = {
                    menuExpanded = false
                },
                onItemClick = { item ->
                    onAction(
                        Preparation.Action.ViewerCountSelect(item = item)
                    )
                    menuExpanded = false
                }
            )
        }
    }
}

@Composable
private fun RecordingSwitcher(
    isRecording: Boolean?,
    onAction: (Preparation.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.preparation_record_live),
            color = FakeLiveTheme.colors.onBackground,
            style = FakeLiveTheme.typography.bodyLarge
        )
        isRecording?.let {
            FakeLiveSwitch(
                checked = isRecording,
                onCheckedChange = { checked ->
                    onAction(
                        Preparation.Action.RecordingUpdate(isRecording = checked)
                    )
                }
            )
        }
    }
}

@LocalePreview
@Composable
private fun PreparationFreePreview() {
    FakeLiveTheme {
        PreparationContent(
            state = Preparation.State(
                newLevel = true,
                image = ImageSource.ResId(R.drawable.img_default_avatar),
                username = "",
                viewerCount = ViewerCount.V_100_200,
                viewerCountList = persistentListOf(),
                isRecording = true,
                premiumStatus = Preparation.PremiumStatus.Free(
                    offerTimer = "12:00:00"
                ),
                showStreamDurationLimitsDialog = false
            ),
            onAction = {}
        )
    }
}

@LocalePreview
@Composable
private fun PreparationPremiumPreview() {
    FakeLiveTheme {
        PreparationContent(
            state = Preparation.State(
                newLevel = false,
                image = ImageSource.ResId(R.drawable.img_default_avatar),
                username = "",
                viewerCount = ViewerCount.V_100_200,
                viewerCountList = persistentListOf(),
                isRecording = false,
                premiumStatus = Preparation.PremiumStatus.Active,
                showStreamDurationLimitsDialog = false
            ),
            onAction = {}
        )
    }
}