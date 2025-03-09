package com.bunbeauty.tiptoplive.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CommentsRequest(
    @SerialName("model") val model: String,
    @SerialName("messages") val messages: List<Message>,
    @SerialName("temperature") val temperature: Int,
    @SerialName("max_tokens") val maxTokens: Int,
    @SerialName("top_p") val topP: Int,
    @SerialName("frequency_penalty") val frequencyPenalty: Int,
    @SerialName("presence_penalty") val presencePenalty: Int,
    @SerialName("response_format") val responseFormat: ResponseFormat
)

@Serializable
class Message(
    val role: String,
    val content: List<Content>
) {

    @Serializable
    class Content(
        @SerialName("type") val type: String,
        @SerialName("text") val text: String? = null,
        @SerialName("image_url") val imageUrl: ImageUrl? = null,
    ) {

        @Serializable
        class ImageUrl(val url: String)

    }

}

@Serializable
class ResponseFormat(
    @SerialName("type") val type: String,
    @SerialName("json_schema") val jsonSchema: JsonSchema
)

@Serializable
class JsonSchema(
    val name: String,
    val strict: Boolean,
    val schema: Schema,
)

@Serializable
class Schema(
    val type: String,
    val additionalProperties: Boolean,
    val required: List<String>,
    val properties: Properties
)

@Serializable
class Properties(
    val comments: Comments
)

@Serializable
class Comments(
    val type: String,
    val items: Items
)

@Serializable
class Items(
    val type: String
)