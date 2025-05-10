package com.bunbeauty.tiptoplive.features.feedback.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base

interface Feedback {

    data class State(
        val feedback: String,
        val sending: Boolean
    ): Base.State

    sealed interface Action: Base.Action {
        data object CloseClick : Action
        data class UpdateFeedback(val text: String) : Action
        data object ImageClick : Action
        data object SendClick : Action
    }

    sealed interface Event: Base.Event {
        data object NavigateBack : Event
        data object NavigateToSuccess : Event
        data object ShowSendingFailed : Event
    }

}