package com.bunbeauty.tiptoplive.features.streamreview.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveFeedbackProvidedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(feedbackProvided: Boolean) {
        keyValueStorage.saveFeedbackProvided(feedbackProvided = feedbackProvided)
    }

}