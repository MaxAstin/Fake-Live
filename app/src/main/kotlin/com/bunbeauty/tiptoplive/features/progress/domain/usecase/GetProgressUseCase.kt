package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueObservableStorage
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.features.progress.domain.model.Level
import com.bunbeauty.tiptoplive.features.progress.domain.model.Progress
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.min

class GetProgressUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage,
    private val keyValueObservableStorage: KeyValueObservableStorage,
    private val getCurrentProgressUseCase: GetCurrentProgressUseCase
) {

    suspend operator fun invoke(): Progress {
        val newAwards = keyValueObservableStorage.getNewAwardsFlow(defaultValue = false).first()
        val showHint = keyValueStorage.getShouldShowProgressHint(defaultValue = true)
        val currentProgress = getCurrentProgressUseCase()
        val nextLevel = Level.entries.find { level ->
            currentProgress.level.number + 1 == level.number
        } ?: Level.entries.last()
        val progress = currentProgress.points.toFloat() / nextLevel.points
        val points = min(currentProgress.points, nextLevel.points)

        return Progress(
            newAwards = newAwards,
            showHint = showHint,
            level = currentProgress.level,
            progress = progress,
            points = points,
            nextLevel = nextLevel
        )
    }

}