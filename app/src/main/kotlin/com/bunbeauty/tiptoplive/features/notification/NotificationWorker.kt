package com.bunbeauty.tiptoplive.features.notification

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.Constants.MAIN_CHANNEL_ID
import com.bunbeauty.tiptoplive.common.util.isNotificationEnabled

private const val NOTIFICATION_ID = 1001

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        // TODO
        // 1. Check on real device after 1 hour / 1 day
        // 2. Set intent to navigate
        // 3. Setup strings
        val builder = NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_camera)
            .setContentTitle("Message from a fan")
            .setContentText("When is the new Live stream?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (context.isNotificationEnabled()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, builder.build())
        }

        return Result.success()
    }
}