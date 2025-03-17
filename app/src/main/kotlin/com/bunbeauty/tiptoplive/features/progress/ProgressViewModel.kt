package com.bunbeauty.tiptoplive.features.progress

import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor() : BaseViewModel<Progress.State, Progress.Action, Progress.Event>(
    initState = {
        Progress.State.Loading
    }
) {

    init {
        setState {
            Progress.State.Success(
                showHint = true,
                level = 1,
                emoji = "\uD83D\uDC23",
                progress = 0f,
                points = 0,
                pointsToNextLevel = 3
            )
        }
    }

    override fun onAction(action: Progress.Action) {
        when (action) {
            Progress.Action.HideHintClick -> {
                val state = currentState as? Progress.State.Success ?: return
                setState {
                    state.copy(showHint = false)
                }
            }
        }
    }
}