package com.bunbeauty.tiptoplive.shared.data.recording

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordingRepository @Inject constructor() {

    private var _recordingUri: String? = null

    fun getRecordingUri(): String? {
        return _recordingUri
    }

    fun saveRecordingUri(uri: String) {
        _recordingUri = uri
    }

}