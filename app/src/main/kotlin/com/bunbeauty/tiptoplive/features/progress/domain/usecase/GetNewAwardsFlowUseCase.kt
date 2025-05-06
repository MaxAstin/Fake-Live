package com.bunbeauty.tiptoplive.features.progress.domain.usecase

import com.bunbeauty.tiptoplive.common.domain.KeyValueObservableStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewAwardsFlowUseCase @Inject constructor(
    private val keyValueObservableStorage: KeyValueObservableStorage
) {

    operator fun invoke(): Flow<Boolean> {
        return keyValueObservableStorage.getNewAwardsFlow(defaultValue = true)
    }

}