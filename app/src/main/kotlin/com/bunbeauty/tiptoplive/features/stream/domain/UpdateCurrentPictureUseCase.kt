package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.features.stream.data.comment.CommentRepository
import javax.inject.Inject

class UpdateCurrentPictureUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {

    operator fun invoke(bytes: ByteArray) {
        commentRepository.updateCurrentPicture(bytes = bytes)
    }
}