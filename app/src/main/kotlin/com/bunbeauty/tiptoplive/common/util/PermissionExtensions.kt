package com.bunbeauty.tiptoplive.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.isNotificationEnabled(): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        || ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS).isGranted()
}

fun Int.isGranted(): Boolean {
    return this == PackageManager.PERMISSION_GRANTED
}