package com.bunbeauty.tiptoplive.features.recording.presentation

import android.net.Uri
import com.bunbeauty.tiptoplive.common.presentation.Base

interface RecordingPreview {

    data class State(
        val videoContent: VideoContent
    ) : Base.State

    sealed interface VideoContent {
        data object Loading : VideoContent
        data class Success(val videoUri: Uri) : VideoContent
    }

    sealed interface Action : Base.Action {
        data object CloseClick : Action
        data object ShareClick : Action
    }

    sealed interface Event : Base.Event {
        data object NavigateBack : Event
        data class OpenSharing(val videoUri: Uri): Event
    }

}