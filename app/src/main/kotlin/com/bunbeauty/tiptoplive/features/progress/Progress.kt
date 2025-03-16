package com.bunbeauty.tiptoplive.features.progress

import com.bunbeauty.tiptoplive.common.presentation.Base

interface Progress {

    sealed interface State: Base.State {
        data object Loading: State
        data class Success(
            val level: Int,
            val emoji: String,
            val progress: Float,
            val points: Int,
            val pointsToNextLevel: Int
        ): State
    }

    sealed interface Action: Base.Action

    sealed interface Event: Base.Event

}