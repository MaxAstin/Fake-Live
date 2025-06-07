package com.bunbeauty.tiptoplive.common.ui.components

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun FakeLiveSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: SwitchColors = SwitchDefaults.colors(
        checkedThumbColor = FakeLiveTheme.colors.background,
        checkedTrackColor = FakeLiveTheme.colors.interactive,
        uncheckedThumbColor = FakeLiveTheme.colors.onBackgroundVariant,
        uncheckedTrackColor = FakeLiveTheme.colors.inactive,
        uncheckedBorderColor = FakeLiveTheme.colors.onBackgroundVariant,
    ),
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = colors
    )
}