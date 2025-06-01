package com.bunbeauty.tiptoplive.features.recording

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private const val RECORDING_CHANNEL_ID = "FakeStreamRecorder"
private const val VIRTUAL_DISPLAY_NAME = "main"

@AndroidEntryPoint
class RecordingService : Service() {

    companion object {
        const val RESULT_CODE_KEY = "result code"
        const val RESULT_DATA_KEY = "result data"
    }

    @Inject
    lateinit var recordingStore: RecordingStore

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaRecorder: MediaRecorder? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, RECORDING_CHANNEL_ID)
            .setContentTitle("Fake Live")
            .setContentText("Fake Live stream is recording")
            .build()
        startForeground(1, notification)

        val resultCode = intent?.extras?.getInt(RESULT_CODE_KEY)
        val resultData = intent?.getParcelableExtra<Intent>(RESULT_DATA_KEY)
        if (resultCode != null && resultData != null) {
            setupVirtualDisplay(
                resultCode = resultCode,
                resultData = resultData
            )
        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            RECORDING_CHANNEL_ID,
            "Fake Stream Recorder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun setupVirtualDisplay(resultCode: Int, resultData: Intent) {
        val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, resultData)
            ?: throw IllegalStateException("MediaProjection unavailable")

        val metrics = resources.displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        val profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P)

        val outputFile = getOutputFile()
        recordingStore.lastRecordingUri = getContentUriForFile(outputFile)

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

        mediaRecorder?.start()
        serviceScope.launch {
            delay(5_000)
            mediaRecorder?.stop()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private fun getOutputFile(): File {
        val mediaDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            ?: filesDir
        val timestamp = System.currentTimeMillis()
        val fileName = "VID_$timestamp.mp4"

        val videoFile = File(mediaDir, fileName)
        if (!videoFile.parentFile!!.exists()) {
            videoFile.parentFile!!.mkdirs()
        }

        return videoFile
    }

    private fun getContentUriForFile(file: File): Uri {
        val authority = "$packageName.fileprovider"
        return FileProvider.getUriForFile(this, authority, file)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

}