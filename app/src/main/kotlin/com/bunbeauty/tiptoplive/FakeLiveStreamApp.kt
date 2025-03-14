package com.bunbeauty.tiptoplive

import android.app.Application
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
    }
}