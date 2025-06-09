package com.bunbeauty.tiptoplive.shared.domain.recording

import com.bunbeauty.tiptoplive.shared.data.recording.RecordingRepository
import jakarta.inject.Inject

class GetRecordingUrlUseCase @Inject constructor(
    private val recordingRepository: RecordingRepository
) {

    operator fun invoke(): String? {
        return recordingRepository.getRecordingUri()
    }

}