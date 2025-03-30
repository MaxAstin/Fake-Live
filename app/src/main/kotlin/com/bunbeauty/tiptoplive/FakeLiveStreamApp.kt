package com.bunbeauty.tiptoplive

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.bunbeauty.tiptoplive.common.Constants.MAIN_CHANNEL_ID
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FakeLiveStreamApp: Application() {

    @Inject
    lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    override fun onCreate() {
        super.onCreate()

        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        createNotificationChannel()
    }

    private fun Context.createNotificationChannel() {
        val channel = NotificationChannel(
            MAIN_CHANNEL_ID,
            "Main (recommended)",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Main channel"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}