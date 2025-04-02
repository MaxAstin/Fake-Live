package com.bunbeauty.tiptoplive.features.stream.domain.model

data class Comment(
    val uuid: String,
    val picture: String?,
    val username: String,
    val text: String,
    val aiGenerated: Boolean
)