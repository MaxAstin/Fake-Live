package com.bunbeauty.tiptoplive.features.streamreview.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base

interface StreamReview {

    object State : Base.State

    sealed interface Action : Base.Action {
        data object CloseClick : Action
        data object LikeClick : Action
        data object DislikeClick : Action
        data object DoNotAskClick : Action
    }

    sealed interface Event : Base.Event {
        data object NavigateBack : Event
        data object OpenMarketListing : Event
        data object NavigateToFeedback : Event
    }

}