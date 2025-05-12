package com.bunbeauty.tiptoplive.features.streamreview.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.preparation.domain.SaveFeedbackProvidedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class StreamReviewViewModel @Inject constructor(
    private val saveFeedbackProvidedUseCase: SaveFeedbackProvidedUseCase,
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
                    saveFeedbackProvidedUseCase(feedbackProvided = true)
                }
                analyticsManager.trackPositiveFeedback()
                sendEvent(StreamReview.Event.OpenMarketListing)
                sendEvent(StreamReview.Event.NavigateBack)
            }
            StreamReview.Action.DislikeClick -> {
                viewModelScope.launch {
                    saveFeedbackProvidedUseCase(feedbackProvided = true)
                }
                analyticsManager.trackNegativeFeedback()
                sendEvent(StreamReview.Event.NavigateToFeedback)
            }
            StreamReview.Action.DoNotAskClick -> {
                viewModelScope.launch {
                    saveFeedbackProvidedUseCase(feedbackProvided = true)
                }
                analyticsManager.trackDoNotAsk()
                sendEvent(StreamReview.Event.NavigateBack)
            }
        }
    }

}