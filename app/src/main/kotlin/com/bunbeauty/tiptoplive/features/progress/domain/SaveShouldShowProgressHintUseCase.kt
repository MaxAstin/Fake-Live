package com.bunbeauty.tiptoplive.features.progress.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveShouldShowProgressHintUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(shouldShowProgressHint: Boolean) {
        keyValueStorage.saveShouldShowProgressHint(shouldShowProgressHint = shouldShowProgressHint)
    }
}