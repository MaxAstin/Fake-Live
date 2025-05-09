package com.bunbeauty.tiptoplive.features.feedback.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.feedback.domain.SendFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val sendFeedbackUseCase: SendFeedbackUseCase
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
            }

            Feedback.Action.SendClick -> {
                viewModelScope.launch {
                    setState { copy(sending = true) }
                    sendFeedbackUseCase.invoke(feedback = state.value.feedback)
                        .onSuccess {
                            sendEvent(Feedback.Event.NavigateToSuccess)
                        }.onFailure {
                            setState {
                                copy(sending = false)
                            }
                            sendEvent(Feedback.Event.ShowSendingFailed)
                        }
                }
            }
        }
    }

}