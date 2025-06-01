package com.bunbeauty.tiptoplive.common.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.bunbeauty.tiptoplive.R
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.core.net.toUri

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
    }.onFailure {
        showToast(message = getString(R.string.common_something_went_wrong))
    }
}

fun Activity.openSharing(uri: Uri) {
    runCatching {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "video/mp4"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share your screen recording"))
    }.onFailure {
        showToast(message = getString(R.string.common_something_went_wrong))
    }
}

fun Activity.openMarketListing() {
    runCatching {
        val uri = "market://details?id=$packageName".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.android.vending")
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            )
        }
        startActivity(intent)
    }.onFailure {
        val intent = Intent(
            Intent.ACTION_VIEW,
            playMarketLink.toUri()
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
