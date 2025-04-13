package com.bunbeauty.tiptoplive.features.preparation.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class GetShowStreamDurationLimitUseCase  @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Boolean {
        return keyValueStorage.getShowStreamDurationLimit(defaultValue = false)
    }
}