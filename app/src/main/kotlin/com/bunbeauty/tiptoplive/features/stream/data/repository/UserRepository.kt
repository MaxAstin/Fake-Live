package com.bunbeauty.tiptoplive.features.stream.data.repository

import com.bunbeauty.tiptoplive.features.stream.data.store.AvatarStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    private val commentAvatarStore = AvatarStore()
    private val questionAvatarStore = AvatarStore()

    fun getCommentAvatarName(): String {
        return commentAvatarStore.getNext()
    }

    fun getQuestionAvatarName(): String {
        return questionAvatarStore.getNext()
    }

}