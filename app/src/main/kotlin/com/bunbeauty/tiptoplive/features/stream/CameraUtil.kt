package com.bunbeauty.tiptoplive.features.stream

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CameraUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsManager: AnalyticsManager
) {

    fun hasCamera(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    fun hasFrontCamera(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
    }

    fun hasBackCamera(): Boolean {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        return cameraManager.cameraIdList.any { cameraId ->
            try {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

                lensFacing == CameraCharacteristics.LENS_FACING_BACK
            } catch (exception: Exception) {
                analyticsManager.trackError(throwable = exception)
                false
            }
        }
    }

}