package com.bunbeauty.tiptoplive.features.preparation.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import kotlinx.collections.immutable.ImmutableList

interface Preparation {

    data class State(
        val newLevel: Boolean,
        val image: ImageSource<*>,
        val username: String,
        val viewerCountList: ImmutableList<ViewerCountItem>,
        val viewerCount: ViewerCount,
        val premiumStatus: PremiumStatus,
        val showStreamDurationLimitsDialog: Boolean?,
    ): Base.State

    data class ViewerCountItem(
        val viewerCount: ViewerCount,
        val isAvailable: Boolean
    )

    sealed interface PremiumStatus {
        data object Loading: PremiumStatus
        data class Free(val offerTimer: String): PremiumStatus
        data object Active: PremiumStatus
    }

    sealed interface Action: Base.Action {
        data object StartScreen: Action
        data object SetupNotification: Action
        data object ProgressClick: Action
        data class ViewerCountSelect(val item: ViewerCountItem): Action
        data class UsernameUpdate(val username: String): Action
        data object AvatarClick: Action
        data object StartStreamClick: Action
        data object CloseStreamDurationLimitsDialogClick: Action
        data object PremiumLaterClick: Action
        data object PremiumClick: Action
    }

    sealed interface Event: Base.Event {
        data object NavigateToProgress: Event
        data object OpenStream: Event
        data object HandlePositiveFeedbackClick: Event
        data object HandleAvatarClick: Event
        data object NavigateToPremiumDetails: Event
    }

}