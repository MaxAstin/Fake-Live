package com.bunbeauty.tiptoplive.features.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DELAYED_NOTIFICATION_TASK_NAME = "delayed notification"

class NotificationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun setupNotification() {
        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context)
            .beginUniqueWork(
                DELAYED_NOTIFICATION_TASK_NAME,
                ExistingWorkPolicy.REPLACE,
                notificationRequest
            )
            .enqueue()
    }


}