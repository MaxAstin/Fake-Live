package com.bunbeauty.tiptoplive.features.stream.data.repository

import android.util.Base64
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveScreenRepository @Inject constructor() {

    var liveScreen: String? = null
        private set

    fun updateLiveScreen(bytes: ByteArray) {
        liveScreen = bytes.toBase64()
    }

    private fun ByteArray.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        outputStream.write(this)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

}