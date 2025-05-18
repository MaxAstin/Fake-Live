package com.bunbeauty.tiptoplive.features.streamreview.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.streamreview.domain.SaveReviewProvidedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class StreamReviewViewModel @Inject constructor(
    private val saveReviewProvidedUseCase: SaveReviewProvidedUseCase,
    private val analyticsManager: AnalyticsManager
) : BaseViewModel<StreamReview.State, StreamReview.Action, StreamReview.Event>(
    initState = {
        StreamReview.State
    }
) {

    override fun onAction(action: StreamReview.Action) {
        when (action) {
            StreamReview.Action.CloseClick -> {
                sendEvent(StreamReview.Event.NavigateBack)
            }
            StreamReview.Action.LikeClick -> {
                viewModelScope.launch {
                    saveReviewProvidedUseCase(provided = true)
                }
                analyticsManager.trackPositiveReview()
                sendEvent(StreamReview.Event.OpenMarketListing)
                sendEvent(StreamReview.Event.NavigateBack)
            }
            StreamReview.Action.DislikeClick -> {
                analyticsManager.trackNegativeReview()
                sendEvent(StreamReview.Event.NavigateToFeedback)
            }
            StreamReview.Action.DoNotAskClick -> {
                viewModelScope.launch {
                    saveReviewProvidedUseCase(provided = true)
                }
                analyticsManager.trackDoNotAskReview()
                sendEvent(StreamReview.Event.NavigateBack)
            }
        }
    }

}