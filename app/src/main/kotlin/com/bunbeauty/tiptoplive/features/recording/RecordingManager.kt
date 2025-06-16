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
import androidx.annotation.OptIn
import androidx.core.content.FileProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.Crop
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.Effects
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.shared.data.recording.RecordingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.File

private const val VIRTUAL_DISPLAY_NAME = "main"
private const val TEMPORARY_FILE_NAME_NAME = "temp.mp4"
private const val RECORDING_FILE_NAME_NAME = "recording.mp4"

@OptIn(UnstableApi::class)
class RecordingManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recordingRepository: RecordingRepository,
    private val analyticsManager: AnalyticsManager
) {

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null
    private var temporaryFile: File? = null
    private var cachedScreenInfo: ScreenInfo? = null

    private var started: Boolean = false

    fun start(
        resultCode: Int,
        resultData: Intent,
        screenInfo: ScreenInfo
    ): Boolean {
        return try {
            val mediaProjectionManager = context.getSystemService(MediaProjectionManager::class.java)
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
                ?: error("MediaProjection unavailable")
            cachedScreenInfo = screenInfo

            val file = getFile(name = TEMPORARY_FILE_NAME_NAME)
            temporaryFile = file

            setupRecorder(
                screenInfo = screenInfo,
                outputFile = file
            )
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
                temporaryFile?.cropVideo()
            } catch (throwable: Throwable) {
                analyticsManager.trackError(throwable = throwable)
            }
        }
    }

    private fun setupRecorder(screenInfo: ScreenInfo, outputFile: File) {
        val profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P)
        mediaRecorder = MediaRecorder().apply {
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFile.absolutePath)
            setVideoSize(screenInfo.widthPx, screenInfo.heightPx)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(profile.videoBitRate)
            setVideoFrameRate(profile.videoFrameRate)
            prepare()
        }
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_DISPLAY_NAME,
            screenInfo.widthPx,
            screenInfo.heightPx,
            context.resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder?.surface,
            null,
            null
        )
    }

    private fun getFile(name: String): File {
        val moviesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: context.filesDir
        val file = File(moviesDirectory, name)
        if (file.parentFile?.exists() == false) {
            file.parentFile?.mkdirs()
        }

        return file
    }

    private fun File.cropVideo() {
        val screenInfo = cachedScreenInfo ?: return

        val recordingFile = getFile(name = RECORDING_FILE_NAME_NAME)
        val listener = getTransformerListener(outputFile = recordingFile)
        val editedMediaItem = getEditedMediaItem(
            inputFile = this,
            screenInfo = screenInfo
        )
        Transformer.Builder(context)
            .addListener(listener)
            .build()
            .start(editedMediaItem, recordingFile.absolutePath)
    }

    private fun getTransformerListener(outputFile: File): Transformer.Listener {
        return object : Transformer.Listener {
            override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                val uri = getContentUriForFile(file = outputFile)
                recordingRepository.saveRecordingUri(uri = uri)
            }

            override fun onError(
                composition: Composition,
                exportResult: ExportResult,
                exportException: ExportException
            ) {
                analyticsManager.trackError(throwable = exportException)
            }
        }
    }

    private fun getEditedMediaItem(inputFile: File, screenInfo: ScreenInfo): EditedMediaItem {
        val inputMediaItem = MediaItem.fromUri(Uri.fromFile(inputFile))
        val bottom = screenInfo.bottomOffsetPx * 2f / screenInfo.widthPx - 1
        val top = (screenInfo.widthPx - screenInfo.topOffsetPx) * 2f / screenInfo.widthPx - 1
        val transformation = Crop(-1f, 1f, bottom, top)
        return EditedMediaItem.Builder(inputMediaItem)
            .setEffects(
                Effects(
                    listOf(),
                    listOf(transformation),
                )
            )
            .build()
    }

    private fun getContentUriForFile(file: File): Uri {
        val authority = "${context.packageName}.fileprovider"
        return FileProvider.getUriForFile(context, authority, file)
    }

}