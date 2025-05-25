package com.bunbeauty.tiptoplive.common.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black200 = Color(0xFF000000)
val Black100 = Color(0xFF1A1A1A)
val Gray400 = Color(0xFF262626)
val Gray300 = Color(0xFF6D6D6D)
val Gray200 = Color(0xFF888888)
val Gray150 = Color(0xFFADADAD)
val Gray100 = Color(0xFFDBDBDB)
val Gray50 = Color(0xFFE9E9E9)
val Blue = Color(0xFF0195F7)
val CornflowerBlue = Color(0xFF737EFA)
val Green = Color(0xFF00BE64)
val Red = Color(0xFFF95667)
val Yellow = Color(0xFFF7CA01)
val Pink = Color(0xFFDB0D67)
val BrightPurple = Color(0xFFCD00BD)
val Scarlet = Color(0xFFFF1E44)
val Amber = Color(0xFFFFBF00)

@Stable
class ColorScheme(
    interactive: Color,
    icon: Color,
    iconVariant: Color,
    surface: Color,
    surfaceVariant: Color,
    selectedSurface: Color,
    onSurface: Color,
    onSurfaceVariant: Color,
    background: Color,
    onBackground: Color,
    onBackgroundVariant: Color,
    border: Color,
    borderVariant: Color,
    positive: Color,
    negative: Color,
    progress: Color,
    inactive: Color,
    premium: Color,
    instagram: InstagramColors,
) {

    var interactive by mutableStateOf(interactive)
        internal set

    var icon by mutableStateOf(icon)
        internal set

    var iconVariant by mutableStateOf(iconVariant)
        internal set

    var surface by mutableStateOf(surface)
        internal set

    var surfaceVariant by mutableStateOf(surfaceVariant)
        internal set

    var selectedSurface by mutableStateOf(selectedSurface)
        internal set

    var onSurface by mutableStateOf(onSurface)
        internal set

    var onSurfaceVariant by mutableStateOf(onSurfaceVariant)
        internal set

    var background by mutableStateOf(background)
        internal set

    var onBackground by mutableStateOf(onBackground)
        internal set

    var onBackgroundVariant by mutableStateOf(onBackgroundVariant)
        internal set

    var border by mutableStateOf(border)
        internal set

    var borderVariant by mutableStateOf(borderVariant)
        internal set

    var positive by mutableStateOf(positive)
        internal set

    var negative by mutableStateOf(negative)
        internal set

    var progress by mutableStateOf(progress)
        internal set

    var inactive by mutableStateOf(inactive)
        internal set

    var premium by mutableStateOf(premium)
        internal set

    var instagram by mutableStateOf(instagram)
        internal set


    fun copy(
        interactive: Color = this.interactive,
        icon: Color = this.icon,
        iconVariant: Color = this.iconVariant,
        surface: Color = this.surface,
        surfaceVariant: Color = this.surfaceVariant,
        selectedSurface: Color = this.selectedSurface,
        onSurface: Color = this.onSurface,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        onBackgroundVariant: Color = this.onBackgroundVariant,
        border: Color = this.border,
        borderVariant: Color = this.borderVariant,
        positive: Color = this.positive,
        negative: Color = this.negative,
        progress: Color = this.progress,
        inactive: Color = this.inactive,
        premium: Color = this.premium,
        instagram: InstagramColors = this.instagram,
    ): ColorScheme = ColorScheme(
        interactive = interactive,
        icon = icon,
        iconVariant = iconVariant,
        surface = surface,
        surfaceVariant = surfaceVariant,
        selectedSurface = selectedSurface,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        background = background,
        onBackground = onBackground,
        onBackgroundVariant = onBackgroundVariant,
        border = border,
        borderVariant = borderVariant,
        positive = positive,
        negative = negative,
        progress = progress,
        inactive = inactive,
        premium = premium,
        instagram = instagram.copy(
            logo1 = instagram.logo1,
            logo2 = instagram.logo2,
            logo3 = instagram.logo3,
            accent = instagram.accent,
        ),
    )
}

@Stable
class InstagramColors(
    logo1: Color,
    logo2: Color,
    logo3: Color,
    accent: Color,
) {
    var logo1 by mutableStateOf(logo1)
        internal set

    var logo2 by mutableStateOf(logo2)
        internal set

    var logo3 by mutableStateOf(logo3)
        internal set

    var accent by mutableStateOf(accent)
        internal set

    fun copy(
        logo1: Color = this.logo1,
        logo2: Color = this.logo2,
        logo3: Color = this.logo3,
        accent: Color = this.accent,
    ): InstagramColors = InstagramColors(
        logo1 = logo1,
        logo2 = logo2,
        logo3 = logo3,
        accent = accent,
    )

}