package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.features.progress.domain.usecase.IncreaseProgressPointsUseCase
import javax.inject.Inject

class FinishDemoStreamAutomaticallyUseCase @Inject constructor(
    private val increaseProgressPointsUseCase: IncreaseProgressPointsUseCase,
    private val keyValueStorage: KeyValueStorage,
    private val analyticsManager: AnalyticsManager
) {

    suspend operator fun invoke() {
        increaseProgressPointsUseCase(points = 1)

        val isNotifiedOfStreamDurationLimit = keyValueStorage.getNotifiedOfStreamDurationLimit(defaultValue = false)
        if (isNotifiedOfStreamDurationLimit) {
            val isFeedbackProvided = keyValueStorage.getFeedbackProvided(defaultValue = false)
            if (!isFeedbackProvided) {
                keyValueStorage.saveShouldAskFeedback(shouldAsk = true)
            }
        } else {
            keyValueStorage.saveShowStreamDurationLimit(show = true)
        }

        analyticsManager.trackStreamAutoFinish()
    }

}