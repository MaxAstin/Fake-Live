package com.bunbeauty.tiptoplive.shared.domain.recording

import android.net.Uri
import com.bunbeauty.tiptoplive.shared.data.recording.RecordingRepository
import jakarta.inject.Inject

class GetRecordingUrlUseCase @Inject constructor(
    private val recordingRepository: RecordingRepository
) {

    operator fun invoke(): Uri? {
        return recordingRepository.getRecordingUri()
    }

}