package com.bunbeauty.tiptoplive.features.stream.data.model

import kotlinx.serialization.Serializable

@Serializable
class CommentsJson(
    val comments: List<String>
)