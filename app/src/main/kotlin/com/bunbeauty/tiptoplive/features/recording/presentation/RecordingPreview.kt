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
        data object ShareClick : Action
    }

    data object Event : Base.Event

}