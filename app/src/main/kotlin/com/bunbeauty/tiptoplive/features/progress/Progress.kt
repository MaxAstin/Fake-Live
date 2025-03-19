package com.bunbeauty.tiptoplive.features.progress

import androidx.annotation.DrawableRes
import com.bunbeauty.tiptoplive.common.presentation.Base

interface Progress {

    sealed interface State: Base.State {
        data object Loading: State
        data class Success(
            val showHint: Boolean,
            val level: Int,
            @DrawableRes val imageId: Int,
            val progress: Float,
            val points: Int,
            val nextLevelPoints: Int
        ): State
    }

    sealed interface Action: Base.Action {
        data object HideHintClick: Action
    }

    sealed interface Event: Base.Event

}