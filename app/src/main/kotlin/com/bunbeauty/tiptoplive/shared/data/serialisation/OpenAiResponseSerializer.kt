package com.bunbeauty.tiptoplive.shared.data.serialisation

import com.bunbeauty.tiptoplive.shared.data.model.OpenAiResponse
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object OpenAiResponseSerializer : JsonContentPolymorphicSerializer<OpenAiResponse>(OpenAiResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<OpenAiResponse> {
        return if ("error" in element.jsonObject) {
            OpenAiResponse.Error.serializer()
        } else {
            OpenAiResponse.Success.serializer()
        }
    }
}