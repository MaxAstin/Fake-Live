package com.bunbeauty.tiptoplive.common.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.bunbeauty.tiptoplive.R
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

fun Activity.openMarketListing() {
    val uri = Uri.parse("market://details?id=$packageName")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.android.vending")
        addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        )
    }

    try {
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(playMarketLink)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
