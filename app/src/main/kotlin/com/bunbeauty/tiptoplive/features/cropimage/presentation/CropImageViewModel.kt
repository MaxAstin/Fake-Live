package com.bunbeauty.tiptoplive.features.cropimage.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.shared.domain.SaveImageUriUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropImageViewModel @Inject constructor(
    private val saveImageUriUseCase: SaveImageUriUseCase
) : BaseViewModel<CropImage.State, CropImage.Action, CropImage.Event>(
    initState = {
        CropImage.State
    }
) {

    override fun onAction(action: CropImage.Action) {
        when (action) {
            is CropImage.Action.CropClick -> {
                viewModelScope.launch {
                    saveImageUriUseCase(uri = action.uri)
                    sendEvent(CropImage.Event.NavigateBack)
                }
            }
        }
    }

}
