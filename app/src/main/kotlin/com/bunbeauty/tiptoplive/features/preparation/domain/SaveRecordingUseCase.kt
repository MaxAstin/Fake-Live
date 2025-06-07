package com.bunbeauty.tiptoplive.features.preparation.domain

import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import javax.inject.Inject

class SaveRecordingUseCase @Inject constructor(
    private val keyValueStorage: KeyValueStorage
) {

    suspend operator fun invoke(isRecording: Boolean) {
        keyValueStorage.saveRecording(isRecording = isRecording)
    }

}