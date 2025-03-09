package com.bunbeauty.tiptoplive.common.data.serialisation

import com.bunbeauty.tiptoplive.common.data.model.CommentsResponse
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object CommentsResponseSerializer : JsonContentPolymorphicSerializer<CommentsResponse>(CommentsResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<CommentsResponse> {
        return if ("error" in element.jsonObject) {
            CommentsResponse.Error.serializer()
        } else {
            CommentsResponse.Success.serializer()
        }
    }
}