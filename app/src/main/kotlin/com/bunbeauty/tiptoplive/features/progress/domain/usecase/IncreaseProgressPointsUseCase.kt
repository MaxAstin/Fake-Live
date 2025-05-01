package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.domain.KeyValueObservableStorage
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class IncreaseProgressPointsUseCase @Inject constructor(
    private val getCurrentProgressUseCase: GetCurrentProgressUseCase,
    private val analyticsManager: AnalyticsManager,
    private val keyValueStorage: KeyValueStorage,
    private val keyValueObservableStorage: KeyValueObservableStorage,
) {

    suspend operator fun invoke(points: Int) {
        val previousProgress = getCurrentProgressUseCase()
        val currentPoints = keyValueStorage.getProgressPoints(defaultValue = 0)
        val updatedPoints = currentPoints + points
        keyValueStorage.saveProgressPoints(points = updatedPoints)

        val updatedProgress = getCurrentProgressUseCase()
        if (previousProgress.level != updatedProgress.level) {
            analyticsManager.trackNewLevel(level = updatedProgress.level.number)
            keyValueObservableStorage.saveNewAwards(hasNewAwards = true)
        }
    }

}