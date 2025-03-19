package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.features.progress.domain.model.Level
import com.bunbeauty.tiptoplive.features.progress.domain.model.Progress
import javax.inject.Inject
import kotlin.math.min

class GetProgressUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Progress {
        val showHint = keyValueStorage.getShouldShowProgressHint(defaultValue = true)
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
        val nextLevel = Level.entries.find { level ->
            currentLevel.number + 1 == level.number
        } ?: Level.entries.last()

        return Progress(
            showHint = showHint,
            level = currentLevel,
            progress = points.toFloat() / nextLevel.points,
            points = min(points, nextLevel.points),
            nextLevel = nextLevel
        )
    }

}