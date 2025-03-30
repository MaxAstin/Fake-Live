package com.bunbeauty.tiptoplive.features.preparation.domain

import com.bunbeauty.tiptoplive.features.notification.NotificationRepository
import javax.inject.Inject

class SetupNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    operator fun invoke() {
        notificationRepository.setupNotification()
    }

}