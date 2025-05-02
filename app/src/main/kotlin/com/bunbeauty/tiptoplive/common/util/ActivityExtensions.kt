package com.bunbeauty.tiptoplive.common.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.util.showToast
import com.google.android.play.core.review.ReviewManagerFactory

fun Activity.launchInAppReview() {
    val reviewManager = ReviewManagerFactory.create(this)
    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            reviewManager.launchReviewFlow(this, task.result)
        }
    }
}

fun Activity.openSettings() {
    startActivity(
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
    )
}

fun Activity.openSharing(text: String) {
    runCatching {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(intent, null)
        startActivity(chooser)
    } .onFailure {
        showToast(message = getString(R.string.common_something_went_wrong))
    }
}