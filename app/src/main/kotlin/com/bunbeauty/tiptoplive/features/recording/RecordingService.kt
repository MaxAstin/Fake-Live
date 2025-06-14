package com.bunbeauty.tiptoplive.features.recording

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bunbeauty.tiptoplive.R
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

private const val RECORDING_CHANNEL_ID = "FakeStreamRecorder"
private const val NOTIFICATION_ID = 1002

@AndroidEntryPoint
class RecordingService : Service() {

    companion object {
        const val RESULT_CODE_KEY = "result code"
        const val RESULT_DATA_KEY = "result data"
        const val WEIGHT_KEY = "weight"
        const val HEIGHT_KEY = "height"
    }

    @Inject
    lateinit var recordingManager: RecordingManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resultCode = intent?.extras?.getInt(RESULT_CODE_KEY)
        val resultData = intent?.getParcelableExtra<Intent>(RESULT_DATA_KEY)
        val weight = intent?.extras?.getInt(WEIGHT_KEY)
        val height = intent?.extras?.getInt(HEIGHT_KEY)

        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        return if (resultCode != null && resultData != null) {
            val isSuccess = recordingManager.start(
                resultCode = resultCode,
                resultData = resultData,
                weight = weight,
                height = height
            )

            if (isSuccess) {
                super.onStartCommand(intent, flags, startId)
            } else {
                startNotSticky()
            }
        } else {
            startNotSticky()
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            RECORDING_CHANNEL_ID,
            getString(R.string.recording_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, RECORDING_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.stream_is_recording))
            .build()
    }

    private fun startNotSticky(): Int {
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        recordingManager.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)

        super.onDestroy()
    }

}