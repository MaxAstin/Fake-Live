package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import com.bunbeauty.tiptoplive.common.util.Seconds
import javax.inject.Inject

private const val STREAM_DURATION_TO_ASK_FEEDBACK = 30 // sec

class ShouldAskFeedbackUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(duration: Seconds): Boolean {
        if (duration.value < STREAM_DURATION_TO_ASK_FEEDBACK) {
            return false
        }

        val provided = keyValueStorage.getFeedbackProvided(defaultValue = false)
        return !provided
    }

}