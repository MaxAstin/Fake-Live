package com.bunbeauty.tiptoplive.features.progress

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.progress.domain.GetProgressUseCase
import com.bunbeauty.tiptoplive.features.progress.domain.SaveShouldShowProgressHintUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getProgressUseCase: GetProgressUseCase,
    private val saveShouldShowProgressHintUseCase: SaveShouldShowProgressHintUseCase,
) : BaseViewModel<Progress.State, Progress.Action, Progress.Event>(
    initState = {
        Progress.State.Loading
    }
) {

    init {
        loadProgress()
    }

    override fun onAction(action: Progress.Action) {
        when (action) {
            Progress.Action.HideHintClick -> {
                val state = currentState as? Progress.State.Success ?: return
                setState {
                    state.copy(showHint = false)
                }
                viewModelScope.launch {
                    saveShouldShowProgressHintUseCase(shouldShowProgressHint = false)
                }
            }
        }
    }

    private fun loadProgress() {
        viewModelScope.launch {
            val progress = getProgressUseCase()
            setState {
                Progress.State.Success(
                    showHint = progress.showHint,
                    level = progress.level,
                    emoji = "\uD83D\uDD25",
                    imageId = R.drawable.img_fire,
                    progress = progress.progress,
                    points = progress.points,
                    nextLevelPoints = progress.nextLevelPoints
                )
            }
        }
    }
}