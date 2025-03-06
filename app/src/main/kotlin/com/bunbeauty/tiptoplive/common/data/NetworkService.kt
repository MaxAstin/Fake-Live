package com.bunbeauty.tiptoplive.common.data

import com.bunbeauty.tiptoplive.common.data.model.ApiResult
import com.bunbeauty.tiptoplive.common.data.model.Comments
import com.bunbeauty.tiptoplive.common.data.model.CommentsRequest
import com.bunbeauty.tiptoplive.common.data.model.CommentsResponse
import com.bunbeauty.tiptoplive.common.data.model.Items
import com.bunbeauty.tiptoplive.common.data.model.JsonSchema
import com.bunbeauty.tiptoplive.common.data.model.Message
import com.bunbeauty.tiptoplive.common.data.model.Properties
import com.bunbeauty.tiptoplive.common.data.model.ResponseFormat
import com.bunbeauty.tiptoplive.common.data.model.Schema
import com.bunbeauty.tiptoplive.common.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class NetworkService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun getComments(
        count: Int,
        language: String,
        currentPicture: String?
    ): ApiResult<CommentsResponse> {
        val systemMessage = Message(
            role = ROLE_SYSTEM,
            content = listOf(
                Message.Content(
                    type = TEXT_TYPE,
                    text = SYSTEM_MESSAGE
                )
            )
        )
        val content = mutableListOf(
            Message.Content(
                type = TEXT_TYPE,
                text = "n = $count, lang = $language"
            )
        )
        if (currentPicture != null) {
            content.add(
                Message.Content(
                    type = IMAGE_URL_TYPE,
                    imageUrl = Message.Content.ImageUrl(
                        url = "data:image/jpeg;base64,$currentPicture"
                    )
                )
            )
        }
        val userMessage = Message(
            role = ROLE_USER,
            content = content
        )
        val request = CommentsRequest(
            model = DEFAULT_MODEL,
            messages = listOf(
                systemMessage,
                userMessage,
            ),
            temperature = DEFAULT_TEMPERATURE,
            maxTokens = DEFAULT_MAX_TOKENS,
            topP = DEFAULT_TOP_P,
            frequencyPenalty = DEFAULT_FREQUENCY_PENALTY,
            presencePenalty = DEFAULT_PRESENCE_PENALTY,
            responseFormat = ResponseFormat(
                type = JSON_TYPE,
                jsonSchema = JsonSchema(
                    name = "comments",
                    strict = true,
                    schema = Schema(
                        type = OBJECT_TYPE,
                        additionalProperties = false,
                        required = listOf("comments"),
                        properties = Properties(
                            comments = Comments(
                                type = ARRAY_TYPE,
                                items = Items(
                                    type = STRING_TYPE
                                )
                            )
                        )
                    )
                )
            )
        )

        return safeCall {
            httpClient.post("completions") {
                setBody(request)
            }
        }
    }

    companion object {

        private const val DEFAULT_MODEL = "gpt-4o-mini"
        private const val DEFAULT_TEMPERATURE = 1
        private const val DEFAULT_MAX_TOKENS = 2048
        private const val DEFAULT_TOP_P = 1
        private const val DEFAULT_FREQUENCY_PENALTY = 0
        private const val DEFAULT_PRESENCE_PENALTY = 0
        private const val TEXT_TYPE = "text"
        private const val IMAGE_URL_TYPE = "image_url"
        private const val JSON_TYPE = "json_schema"
        private const val OBJECT_TYPE = "object"
        private const val ARRAY_TYPE = "array"
        private const val STRING_TYPE = "string"

        private const val ROLE_SYSTEM = "system"
        private const val ROLE_USER = "user"

        private val SYSTEM_MESSAGE = """
            Create comments for Live stream where blogger is just talking about something.
            Ignore any people faces on the image and make up comments based on the background, clothes, objects and environment on the image from the Live stream.
            Comments should contain a choice of either  1 interjection, 1 word or 2-6 words.
            Comments must contain emoji with a probability of 50%.
            Do not use exclamation marks.
            
            [
              Comments count: n, 
              Language: lang
            ]
        """.trimIndent()

    }
}