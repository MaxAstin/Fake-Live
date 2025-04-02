package com.bunbeauty.tiptoplive.features.preparation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.bunbeauty.tiptoplive.common.util.isNotificationEnabled

@Composable
fun RequestNotificationsPermission(
    onGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onGranted()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (context.isNotificationEnabled()) {
            onGranted()
        } else {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}