package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.common.util.Seconds
import javax.inject.Inject

private const val STREAM_DURATION_TO_ASK_FEEDBACK = 30 // sec

class FinishStreamManuallyUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage,
    private val analyticsManager: AnalyticsManager
) {

    suspend operator fun invoke(duration: Seconds) {
        val feedbackProvided = keyValueStorage.getFeedbackProvided(defaultValue = false)
        if (duration.value >= STREAM_DURATION_TO_ASK_FEEDBACK && !feedbackProvided) {
            keyValueStorage.saveShouldAskFeedback(shouldAsk = true)
        }
        analyticsManager.trackStreamFinish(duration = duration)
    }

}