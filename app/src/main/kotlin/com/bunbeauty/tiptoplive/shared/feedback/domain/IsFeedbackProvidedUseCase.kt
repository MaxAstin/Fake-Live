package com.bunbeauty.tiptoplive.shared.feedback.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class IsFeedbackProvidedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Boolean {
        return keyValueStorage.getFeedbackProvided(defaultValue = false)
    }

}