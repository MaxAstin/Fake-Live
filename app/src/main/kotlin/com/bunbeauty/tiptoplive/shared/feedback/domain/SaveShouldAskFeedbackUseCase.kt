package com.bunbeauty.tiptoplive.shared.feedback.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveShouldAskFeedbackUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(shouldAsk: Boolean) {
        keyValueStorage.saveShouldAskFeedback(shouldAsk = shouldAsk)
    }

}