package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveNewLevelUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(newLevel: Boolean) {
        keyValueStorage.saveNewLevel(newLevel = newLevel)
    }

}