package com.bunbeauty.tiptoplive.common.util

import android.content.Context
import android.widget.Toast

val Context.playMarketLink
    get() = "https://play.google.com/store/apps/details?id=$packageName"

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
        .show()
}