package com.bunbeauty.tiptoplive.features.recording.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.shared.domain.recording.GetRecordingUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordingPreviewViewModel @Inject constructor(
    private val getRecordingUriUseCase: GetRecordingUrlUseCase
) : BaseViewModel<RecordingPreview.State, RecordingPreview.Action, RecordingPreview.Event>(
    initState = {
        RecordingPreview.State(videoContent = RecordingPreview.VideoContent.Loading)
    }
) {

    init {
        viewModelScope.launch {
            val uri = getRecordingUriUseCase()
            if (uri != null) {
                setState {
                    copy(videoContent = RecordingPreview.VideoContent.Success(videoUri = uri))
                }
            }
        }
    }

    override fun onAction(action: RecordingPreview.Action) {
        when (action) {
            RecordingPreview.Action.CloseClick -> {
                sendEvent(RecordingPreview.Event.NavigateBack)
            }

            RecordingPreview.Action.ShareClick -> {
                // TODO track sharing click
                val videoUri = (state.value.videoContent as? RecordingPreview.VideoContent.Success)?.videoUri ?: return
                sendEvent(
                    RecordingPreview.Event.OpenSharing(videoUri = videoUri)
                )
            }
        }
    }

}