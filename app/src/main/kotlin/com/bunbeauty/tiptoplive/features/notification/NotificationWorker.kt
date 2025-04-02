package com.bunbeauty.tiptoplive.features.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.Constants.MAIN_CHANNEL_ID
import com.bunbeauty.tiptoplive.common.Constants.NOTIFICATION_MESSAGE_KEY
import com.bunbeauty.tiptoplive.common.util.isNotificationEnabled
import com.bunbeauty.tiptoplive.features.main.MainActivity

private const val NOTIFICATION_ID = 1001

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val notificationMessage = NotificationMessage.entries.random()
        val intent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(NOTIFICATION_MESSAGE_KEY, notificationMessage)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_live)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(notificationMessage.messageId))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (context.isNotificationEnabled()) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, builder.build())
        }

        return Result.success()
    }
}