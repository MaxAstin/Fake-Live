package com.bunbeauty.tiptoplive.common.data.model

import com.bunbeauty.tiptoplive.common.data.serialisation.CommentsResponseSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(CommentsResponseSerializer::class)
sealed interface CommentsResponse {

    @Serializable
    data class Success(
        @SerialName("id") val id: String,
        @SerialName("object") val objectName: String,
        @SerialName("created") val created: Int,
        @SerialName("model") val model: String,
        @SerialName("choices") val choices: List<Choice>,
        @SerialName("usage") val usage: Usage,
        @SerialName("system_fingerprint") val systemFingerprint: String
    ) : CommentsResponse

    @Serializable
    data class Error(val error: ErrorDetails) : CommentsResponse {

        @Serializable
        data class ErrorDetails(
            val message: String,
            val type: String,
            val param: String,
            val code: String
        )
    }
}

@Serializable
data class Choice(
    @SerialName("index") val index: Int,
    @SerialName("message") val message: Message,
    @SerialName("finish_reason") val finishReason: String
) {

    @Serializable
    data class Message(
        @SerialName("role") val role: String,
        @SerialName("content") val content: String
    )
}

@Serializable
data class Usage(
    @SerialName("prompt_tokens") val promptTokens: Int,
    @SerialName("completion_tokens") val completionTokens: Int,
    @SerialName("total_tokens") val totalTokens: Int
)