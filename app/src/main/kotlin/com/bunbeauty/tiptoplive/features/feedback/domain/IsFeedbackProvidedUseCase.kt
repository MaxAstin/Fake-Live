package com.bunbeauty.tiptoplive.features.feedback.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.common.util.getCurrentTimeSeconds
import com.bunbeauty.tiptoplive.common.util.toDateString
import javax.inject.Inject

class IsFeedbackProvidedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke() : Boolean {
        val currentDate = getCurrentTimeSeconds().toDateString()
        val lastFeedbackDate = keyValueStorage.getLastFeedbackDate()
        return currentDate == lastFeedbackDate
    }

}