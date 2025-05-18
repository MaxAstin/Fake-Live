package com.bunbeauty.tiptoplive.features.streamreview.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveReviewProvidedUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(provided: Boolean) {
        keyValueStorage.saveReviewProvided(provided = provided)
    }

}