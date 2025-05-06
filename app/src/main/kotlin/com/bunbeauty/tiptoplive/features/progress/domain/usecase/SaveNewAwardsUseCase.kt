package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueObservableStorage
import javax.inject.Inject

class SaveNewAwardsUseCase @Inject constructor(
    private val keyValueObservableStorage: KeyValueObservableStorage
) {

    suspend operator fun invoke(hasNewAwards: Boolean) {
        keyValueObservableStorage.saveNewAwards(hasNewAwards = hasNewAwards)
    }

}