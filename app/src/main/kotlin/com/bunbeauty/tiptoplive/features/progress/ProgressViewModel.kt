package com.bunbeauty.tiptoplive.features.progress

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.progress.domain.model.Level
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.GetProgressUseCase
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.SaveNewLevelUseCase
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.SaveShouldShowProgressHintUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val saveNewLevelUseCase: SaveNewLevelUseCase,
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
            Progress.Action.HintClick -> {
                updateHintVisibility()
            }
            Progress.Action.EmojiClick -> {
                updateHintVisibility()
            }
        }
    }

    private fun loadProgress() {
        viewModelScope.launch {
            val progress = getProgressUseCase()
            setState {
                Progress.State.Success(
                    showNewLevelAnimation = progress.newLevel,
                    showHint = progress.showHint,
                    level = progress.level.number,
                    imageId = progress.level.imageId(),
                    progress = progress.progress,
                    points = progress.points,
                    nextLevelPoints = progress.nextLevel.points
                )
            }

            resetNewLevel()
        }
    }

    private fun updateHintVisibility() {
        val state = currentState as? Progress.State.Success ?: return
        val showHint = state.showHint

        setState {
            state.copy(showHint = !showHint)
        }
        if (showHint) {
            viewModelScope.launch {
                saveShouldShowProgressHintUseCase(shouldShowProgressHint = false)
            }
        }
    }

    private fun resetNewLevel() {
        viewModelScope.launch {
            saveNewLevelUseCase(newLevel = false)
        }
    }

    private fun Level.imageId(): Int {
        return when (this) {
            Level.LEVEL_1 -> R.drawable.img_chick
            Level.LEVEL_2 -> R.drawable.img_ghost
            Level.LEVEL_3 -> R.drawable.img_party_popper
            Level.LEVEL_4 -> R.drawable.img_shooting_star
            Level.LEVEL_5 -> R.drawable.img_sparkles
            Level.LEVEL_6 -> R.drawable.img_sparkling_heart
            Level.LEVEL_7 -> R.drawable.img_heart_on_fire
            Level.LEVEL_8 -> R.drawable.img_fire
            Level.LEVEL_9 -> R.drawable.img_biceps
            Level.LEVEL_10 -> R.drawable.img_crown
        }
    }
}