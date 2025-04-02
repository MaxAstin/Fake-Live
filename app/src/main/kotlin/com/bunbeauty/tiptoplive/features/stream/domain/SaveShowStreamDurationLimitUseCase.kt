package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveShowStreamDurationLimitUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(show: Boolean) {
        keyValueStorage.saveShowStreamDurationLimit(show = show)
    }

}