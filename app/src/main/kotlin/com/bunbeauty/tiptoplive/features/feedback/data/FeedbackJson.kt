package com.bunbeauty.tiptoplive.features.feedback.data

import androidx.annotation.Keep

@Keep
class FeedbackJson(
    val text: String,
    val language: String,
    val isPremium: Boolean
)