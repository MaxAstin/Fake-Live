package com.bunbeauty.tiptoplive.features.recording

import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.shared.data.recording.RecordingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.File

private const val VIRTUAL_DISPLAY_NAME = "main"
private const val OUTPUT_FILE_NAME_NAME = "recording.mp4"

class RecordingManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordingRepository: RecordingRepository,
    private val analyticsManager: AnalyticsManager
) {

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null

    private var started: Boolean = false

    fun start(resultCode: Int, resultData: Intent): Boolean {
        return try {
            val mediaProjectionManager = context.getSystemService(MediaProjectionManager::class.java)
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
                ?: error("MediaProjection unavailable")

            setupRecorder()
            mediaRecorder?.start()
            started = true

            true
        } catch (throwable: Throwable) {
            analyticsManager.trackError(throwable = throwable)

            false
        }
    }

    fun stop() {
        if (started) {
            started = false
            try {
                mediaRecorder?.stop()
            } catch (throwable: Throwable) {
                analyticsManager.trackError(throwable = throwable)
            }
        }
    }

    private fun setupRecorder() {
        val metrics = context.resources.displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        val profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P)

        val outputFile = getOutputFile()
        recordingRepository.saveRecordingUri(
            uri = getContentUriForFile(
                file = outputFile
            )
        )

        mediaRecorder = MediaRecorder().apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFile.absolutePath)
            setVideoSize(screenWidth, screenHeight)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(profile.videoBitRate)
            setVideoFrameRate(profile.videoFrameRate)
            prepare()
        }
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_DISPLAY_NAME,
            screenWidth,
            screenHeight,
            metrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder?.surface,
            null,
            null
        )
    }

    private fun getOutputFile(): File {
        val moviesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: context.filesDir
        val outputFile = File(moviesDirectory, OUTPUT_FILE_NAME_NAME)
        if (outputFile.parentFile?.exists() == false) {
            outputFile.parentFile?.mkdirs()
        }

        return outputFile
    }

    private fun getContentUriForFile(file: File): Uri {
        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }

}