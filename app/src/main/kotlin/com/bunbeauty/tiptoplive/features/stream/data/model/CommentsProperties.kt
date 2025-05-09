package com.bunbeauty.tiptoplive.features.stream.data.model

import com.bunbeauty.tiptoplive.shared.data.model.Properties
import kotlinx.serialization.Serializable

@Serializable
class CommentsProperties(
    val comments: Comments
): Properties() {

    @Serializable
    class Comments(
        val type: String,
        val items: Items
    )

    @Serializable
    class Items(
        val type: String
    )

}
