package com.bunbeauty.tiptoplive.features.stream.domain.model

data class Question(
    val uuid: String,
    val avatarName: String?,
    val username: String,
    val text: String,
)