package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.stream.view.ViewersCountUi

@Composable
fun StreamHeader(
    image: ImageSource<*>,
    username: String,
    viewersCount: ViewersCountUi,
    actionEnabled: Boolean,
    onClose: () -> Unit,
    onSwitchCamera: () -> Unit,
    onCameraClick: () -> Unit,
    onFiltersClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier ,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarImage(
                image = image,
                modifier = Modifier.size(32.dp)
            )
            UsernameRow(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                username = username
            )

            LiveCard(modifier = Modifier.padding(start = 12.dp))
            ViewersCard(
                modifier = Modifier.padding(start = 8.dp),
                viewersCount = viewersCount
            )
            ActionIcon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickableWithoutIndication(
                        onClick = {
                            onClose()
                        }
                    ),
                iconResId = R.drawable.ic_close,
                contentDescription = "Close",
            )
        }
        Actions(
            modifier = Modifier.padding(top = 16.dp),
            enabled = actionEnabled,
            onSwitchClick = onSwitchCamera,
            onCameraClick = onCameraClick,
            onFiltersClick = onFiltersClick
        )
    }
}

@Composable
private fun UsernameRow(
    username: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f, false),
            text = username,
            color = FakeLiveTheme.colors.onSurface,
            style = FakeLiveTheme.typography.titleSmall,
            overflow = Ellipsis,
            maxLines = 1,
        )
        Icon(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(16.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_down),
            contentDescription = "Dropdown",
            tint = FakeLiveTheme.colors.icon,
        )
    }
}

@Composable
private fun LiveCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FakeLiveTheme.colors.instagram.accent)
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.stream_live),
            style = FakeLiveTheme.typography.bodySmall,
            color = FakeLiveTheme.colors.onSurface
        )
    }
}

@Composable
private fun ViewersCard(
    viewersCount: ViewersCountUi,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FakeLiveTheme.colors.surface.copy(alpha = 0.5f))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_eye),
                contentDescription = "Viewers",
                tint = FakeLiveTheme.colors.icon,
            )
            val viewersCountText = when (viewersCount) {
                is ViewersCountUi.UpToThousand -> viewersCount.count
                is ViewersCountUi.Thousands -> stringResource(
                    R.string.stream_thousands_viewers_count,
                    viewersCount.thousands,
                    viewersCount.hundreds
                )
            }
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = viewersCountText,
                style = FakeLiveTheme.typography.bodySmall,
                color = FakeLiveTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun Actions(
    onSwitchClick: () -> Unit,
    onCameraClick: () -> Unit,
    onFiltersClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(16.dp)
    ) {
        var isMicMuted by remember {
            mutableStateOf(false)
        }
        var isCameraEnabled by remember {
            mutableStateOf(true)
        }

        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                enabled = enabled,
                onClick = {
                    isMicMuted = !isMicMuted
                }
            ),
            iconResId = if (isMicMuted) {
                R.drawable.ic_mic_crossed_out
            } else {
                R.drawable.ic_mic
            },
            contentDescription = "Mic",
        )
        ActionIcon(
            modifier = Modifier.clickableWithoutIndication(
                enabled = enabled,
                onClick = {
                    isCameraEnabled = !isCameraEnabled
                    onCameraClick()
                }
            ),
            iconResId = if (isCameraEnabled) {
                R.drawable.ic_camera
            } else {
                R.drawable.ic_camera_crossed_out
            },
            contentDescription = "Camera",
        )
        if (isCameraEnabled) {
            ActionIcon(
                modifier = Modifier.clickableWithoutIndication(
                    enabled = enabled,
                    onClick = onSwitchClick
                ),
                iconResId = R.drawable.ic_switch,
                contentDescription = "Switch camera",
            )
            ActionIcon(
                modifier = Modifier.clickableWithoutIndication(
                    enabled = enabled,
                    onClick = {
                        onFiltersClick()
                    }
                ),
                iconResId = R.drawable.ic_effect,
                contentDescription = "Effect",
            )
        }
    }
}