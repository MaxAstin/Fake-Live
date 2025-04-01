package com.bunbeauty.tiptoplive.features.cropimage.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base

interface CropImage {

    data object State: Base.State

    sealed interface Action: Base.Action {
        data class CropClick(val uri: String): Action
    }

    sealed interface Event: Base.Event {
        data object NavigateBack: Event
    }

}