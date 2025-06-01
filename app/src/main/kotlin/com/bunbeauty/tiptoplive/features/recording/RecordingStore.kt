package com.bunbeauty.tiptoplive.features.recording

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordingStore @Inject constructor() {

    var lastRecordingUri: Uri? = null

}