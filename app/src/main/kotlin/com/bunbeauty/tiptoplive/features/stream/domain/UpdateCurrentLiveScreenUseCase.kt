package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.features.stream.data.repository.LiveScreenRepository
import javax.inject.Inject

class UpdateCurrentLiveScreenUseCase @Inject constructor(
    private val liveScreenRepository: LiveScreenRepository
) {

    operator fun invoke(bytes: ByteArray) {
        liveScreenRepository.updateLiveScreen(bytes = bytes)
    }
}