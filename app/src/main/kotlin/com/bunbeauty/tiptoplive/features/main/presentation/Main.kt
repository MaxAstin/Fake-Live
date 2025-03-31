package com.bunbeauty.tiptoplive.features.main.presentation

import android.net.Uri
import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.features.notification.NotificationMessage

interface Main {

    data class State(
        val showNoCameraPermission: Boolean
    ): Base.State

    sealed interface Action: Base.Action {
        data class NotificationClick(val notificationMessage: NotificationMessage): Action
        data object AppStart: Action
        data object AppStop: Action
        data object CameraPermissionDeny: Action
        data object CameraPermissionAccept: Action
        data object CloseCameraRequiredDialogClick: Action
        data class AvatarSelected(val uri: Uri?): Action
    }

    sealed interface Event: Base.Event {
        data object OpenStream: Event
    }

}