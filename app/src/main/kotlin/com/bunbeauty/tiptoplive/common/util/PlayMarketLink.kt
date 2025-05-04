package com.bunbeauty.tiptoplive.common.util

import android.content.Context

val Context.playMarketLink
    get() = "https://play.google.com/store/apps/details?id=$packageName"