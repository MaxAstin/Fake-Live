package com.bunbeauty.tiptoplive.features.feedback.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
) : BaseViewModel<Feedback.State, Feedback.Action, Feedback.Event>(
    initState = {
        Feedback.State(
            feedback = "",
            sending = false
        )
    }
) {

    override fun onAction(action: Feedback.Action) {
        when (action) {
            Feedback.Action.CloseClick -> {
                sendEvent(Feedback.Event.NavigateBack)
            }

            is Feedback.Action.UpdateFeedback -> {
                setState {
                    copy(feedback = action.text)
                }
            }

            Feedback.Action.ImageClick -> {
                // TODO attach image
                setState {
                    copy(feedback = "")
                }
            }

            Feedback.Action.SendClick -> {
                viewModelScope.launch {
                    setState { copy(sending = true) }
                    delay(2_000) // TODO send to Firebase
                    sendEvent(Feedback.Event.ShowSuccessfullySent)
                    sendEvent(Feedback.Event.NavigateBack)
                }
            }
        }
    }

}