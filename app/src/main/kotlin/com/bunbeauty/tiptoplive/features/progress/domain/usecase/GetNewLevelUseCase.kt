package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class GetNewLevelUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(): Boolean {
        return keyValueStorage.getNewLevel(defaultValue = true)
    }

}