package com.bunbeauty.tiptoplive.features.preparation.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveNotifiedOfStreamDurationLimitUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(notified: Boolean) {
        keyValueStorage.saveNotifiedOfStreamDurationLimit(notified = notified)
    }

}