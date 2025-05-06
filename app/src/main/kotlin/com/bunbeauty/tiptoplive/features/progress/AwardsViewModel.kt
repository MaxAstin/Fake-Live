package com.bunbeauty.tiptoplive.features.progress

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.progress.domain.model.Level
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.GetProgressUseCase
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.SaveNewAwardsUseCase
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.SaveShouldShowProgressHintUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AwardsViewModel @Inject constructor(
    private val saveNewAwardsUseCase: SaveNewAwardsUseCase,
    private val getProgressUseCase: GetProgressUseCase,
    private val saveShouldShowProgressHintUseCase: SaveShouldShowProgressHintUseCase,
) : BaseViewModel<Awards.State, Awards.Action, Awards.Event>(
    initState = {
        Awards.State.Loading
    }
) {

    init {
        getProgress()
    }

    override fun onAction(action: Awards.Action) {
        when (action) {
            Awards.Action.HintClick -> {
                updateHintVisibility()
            }
            Awards.Action.EmojiClick -> {
                updateHintVisibility()
            }
        }
    }

    private fun getProgress() {
        viewModelScope.launch {
            val progress = getProgressUseCase()
            setState {
                Awards.State.Success(
                    showNewAwardsAnimation = progress.newAwards,
                    showHint = progress.showHint,
                    level = progress.level.number,
                    imageId = progress.level.imageId(),
                    progress = progress.progress,
                    points = progress.points,
                    nextLevelPoints = progress.nextLevel.points
                )
            }

            saveNewAwardsUseCase(hasNewAwards = false)
        }
    }

    private fun updateHintVisibility() {
        val state = currentState as? Awards.State.Success ?: return
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