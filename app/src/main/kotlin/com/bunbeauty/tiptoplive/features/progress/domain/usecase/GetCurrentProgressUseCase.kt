package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.features.progress.domain.model.CurrentProgress
import com.bunbeauty.tiptoplive.features.progress.domain.model.Level
import javax.inject.Inject

class GetCurrentProgressUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): CurrentProgress {
        var points = keyValueStorage.getProgressPoints(defaultValue = 0)
        var currentLevel = Level.LEVEL_1
        Level.entries.forEach { level ->
            if (points >= level.points) {
                points -= level.points
                currentLevel = level
            } else {
                return@forEach
            }
        }

        return CurrentProgress(
            level = currentLevel,
            points = points
        )
    }

}