package com.bunbeauty.tiptoplive.features.more.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base

interface More {

    data class State(
        val premium: Premium
    ): Base.State

    sealed interface Premium {
        data object Loading: Premium
        data object Active: Premium
        data class Discount(
            val timer: String
        ): Premium
    }

    sealed interface Action: Base.Action {
        data object PremiumClick: Action
    }

    sealed interface Event: Base.Event {
        data object NavigateToPremiumDetails: Event
    }


}