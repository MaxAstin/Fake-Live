package com.bunbeauty.tiptoplive.features.stream.data.mapper

import com.bunbeauty.tiptoplive.shared.data.model.OpenAiResponse

fun OpenAiResponse.toContent(): Result<String> {
    when (this) {
        is OpenAiResponse.Success -> {
            val content = this.choices.last { choice ->
                choice.message.role == "assistant"
            }.message.content

            return Result.success(content)
        }

        is OpenAiResponse.Error -> {
            return Result.failure(Exception(error.message))
        }
    }
}
