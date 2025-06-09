package com.bunbeauty.tiptoplive.shared.data.recording

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordingRepository @Inject constructor() {

    private var _recordingUri: Uri? = null

    fun getRecordingUri(): Uri? {
        return _recordingUri
    }

    fun saveRecordingUri(uri: Uri) {
        _recordingUri = uri
    }

}