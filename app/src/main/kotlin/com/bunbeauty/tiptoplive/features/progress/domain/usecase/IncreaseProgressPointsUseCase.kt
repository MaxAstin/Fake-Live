package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class IncreaseProgressPointsUseCase @Inject constructor(
    private val getProgressUseCase: GetProgressUseCase,
    private val analyticsManager: AnalyticsManager,
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(points: Int) {
        val previousProgress = getProgressUseCase()
        val currentPoints = keyValueStorage.getProgressPoints(defaultValue = 0)
        val updatedPoints = currentPoints + points
        keyValueStorage.saveProgressPoints(points = updatedPoints)

        val updatedProgress = getProgressUseCase()
        if (previousProgress.level != updatedProgress.level) {
            analyticsManager.trackNewLevel(level = updatedProgress.level.number)
            keyValueStorage.saveNewLevel(newLevel = true)
        }
    }

}