package com.bunbeauty.tiptoplive.features.notification

import androidx.annotation.StringRes
import com.bunbeauty.tiptoplive.R

enum class NotificationMessage(@StringRes val messageId: Int) {
    WAITING(messageId = R.string.notification_body_waiting),
    START_YOUR_LIVE(messageId = R.string.notification_body_start_your_live),
    WILL_YOU_GO(messageId = R.string.notification_body_will_you_go),
    WHEN_NEXT(messageId = R.string.notification_body_when_next)
}