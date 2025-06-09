package com.bunbeauty.tiptoplive.features.recording.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base

interface Recording {

    object State: Base.State

    sealed interface Action: Base.Action {
        data object ShareClick: Action
    }

    data object Event: Base.Event

}