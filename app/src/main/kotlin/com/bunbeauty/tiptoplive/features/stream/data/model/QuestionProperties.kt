package com.bunbeauty.tiptoplive.features.stream.data.model

import com.bunbeauty.tiptoplive.shared.data.model.Properties
import kotlinx.serialization.Serializable

@Serializable
class QuestionProperties(
    val questions: Questions
): Properties() {

    @Serializable
    class Questions(
        val type: String,
        val items: Items
    )

    @Serializable
    class Items(
        val type: String
    )
}
