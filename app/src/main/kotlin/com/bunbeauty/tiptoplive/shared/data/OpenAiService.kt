package com.bunbeauty.tiptoplive.shared.data

import com.bunbeauty.tiptoplive.common.data.model.ApiResult
import com.bunbeauty.tiptoplive.common.util.safeCall
import com.bunbeauty.tiptoplive.features.stream.data.model.CommentsProperties
import com.bunbeauty.tiptoplive.features.stream.data.model.QuestionProperties
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.ARRAY_TYPE
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.DEFAULT_FREQUENCY_PENALTY
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.DEFAULT_MAX_TOKENS
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.DEFAULT_MODEL
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.DEFAULT_PRESENCE_PENALTY
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.DEFAULT_TEMPERATURE
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.DEFAULT_TOP_P
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.IMAGE_URL_TYPE
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.JSON_TYPE
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.OBJECT_TYPE
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.ROLE_SYSTEM
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.ROLE_USER
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.STRING_TYPE
import com.bunbeauty.tiptoplive.shared.data.OpenAiConstants.TEXT_TYPE
import com.bunbeauty.tiptoplive.shared.data.model.JsonSchema
import com.bunbeauty.tiptoplive.shared.data.model.Message
import com.bunbeauty.tiptoplive.shared.data.model.OpenAiRequest
import com.bunbeauty.tiptoplive.shared.data.model.OpenAiResponse
import com.bunbeauty.tiptoplive.shared.data.model.ResponseFormat
import com.bunbeauty.tiptoplive.shared.data.model.Schema
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class OpenAiService @Inject constructor(
    private val httpClient: HttpClient
) {

    suspend fun getComments(
        count: Int,
        language: String,
        image: String?
    ): ApiResult<OpenAiResponse> {
        val request = getRequest(
            systemMessageText = "$BASIC_COMMENT_PROMPT ${VIBE_DESCRIPTION_LIST.random()} $SYSTEM_MESSAGE_PARAMS",
            userMessageText = "n = $count, lang = $language",
            userImage = image,
            jsonSchema = getCommentsJsonSchema()
        )

        return safeCall {
            httpClient.post("completions") {
                setBody(request)
            }
        }
    }

    suspend fun getQuestions(
        count: Int,
        language: String,
        image: String?
    ): ApiResult<OpenAiResponse> {
        val request = getRequest(
            systemMessageText = "$BASIC_QUESTION_PROMPT ${VIBE_DESCRIPTION_LIST.random()} $QUESTION_RULES",
            userMessageText = "n = $count, lang = $language",
            userImage = image,
            jsonSchema = getQuestionJsonSchema()
        )

        return safeCall {
            httpClient.post("completions") {
                setBody(request)
            }
        }
    }

    private fun getRequest(
        systemMessageText: String,
        userMessageText: String,
        userImage: String?,
        jsonSchema: JsonSchema
    ): OpenAiRequest {
        val systemMessage = getSystemMessage(text = systemMessageText)
        val userMessage = getUserMessage(
            text = userMessageText,
            imageUrl = userImage?.let { "data:image/jpeg;base64,$it" }
        )

        return OpenAiRequest(
            model = DEFAULT_MODEL,
            messages = listOf(
                systemMessage,
                userMessage
            ),
            temperature = DEFAULT_TEMPERATURE,
            maxTokens = DEFAULT_MAX_TOKENS,
            topP = DEFAULT_TOP_P,
            frequencyPenalty = DEFAULT_FREQUENCY_PENALTY,
            presencePenalty = DEFAULT_PRESENCE_PENALTY,
            responseFormat = ResponseFormat(
                type = JSON_TYPE,
                jsonSchema = jsonSchema
            )
        )
    }

    private fun getSystemMessage(text: String): Message {
        return Message(
            role = ROLE_SYSTEM,
            content = listOf(
                Message.Content(
                    type = TEXT_TYPE,
                    text = text
                )
            )
        )
    }

    private fun getUserMessage(text: String, imageUrl: String?): Message {
        val content = mutableListOf(
            Message.Content(
                type = TEXT_TYPE,
                text = text
            )
        )
        if (imageUrl != null) {
            content.add(
                Message.Content(
                    type = IMAGE_URL_TYPE,
                    imageUrl = Message.Content.ImageUrl(
                        url = imageUrl
                    )
                )
            )
        }
        return Message(
            role = ROLE_USER,
            content = content
        )
    }

    private fun getCommentsJsonSchema(): JsonSchema {
        return JsonSchema(
            name = COMMENTS_JSON_SCHEMA_NAME,
            strict = true,
            schema = Schema(
                type = OBJECT_TYPE,
                additionalProperties = false,
                required = listOf(COMMENTS_ARRAY_NAME),
                properties = CommentsProperties(
                    comments = CommentsProperties.Comments(
                        type = ARRAY_TYPE,
                        items = CommentsProperties.Items(
                            type = STRING_TYPE
                        )
                    )
                )
            )
        )
    }

    private fun getQuestionJsonSchema(): JsonSchema {
        return JsonSchema(
            name = QUESTION_JSON_SCHEMA_NAME,
            strict = true,
            schema = Schema(
                type = OBJECT_TYPE,
                additionalProperties = false,
                required = listOf(QUESTION_ARRAY_NAME),
                properties = QuestionProperties(
                    questions = QuestionProperties.Questions(
                        type = ARRAY_TYPE,
                        items = QuestionProperties.Items(
                            type = STRING_TYPE
                        )
                    )
                )
            )
        )
    }

    companion object {

        private const val COMMENTS_JSON_SCHEMA_NAME = "comments"
        private const val COMMENTS_ARRAY_NAME = "comments"

        private const val QUESTION_JSON_SCHEMA_NAME = "questions"
        private const val QUESTION_ARRAY_NAME = "questions"

        private val BASIC_COMMENT_PROMPT = """
            Create comments for Live stream where blogger is just talking about something.
            Ignore any people faces on the image and make up comments based on the background, clothes, objects and environment on the image from the Live stream.
            Comments should contain a choice of either 1 interjection, 1 word or 2-6 words.
            Comments can contain emoji (50% chance).
            Do not use exclamation marks.
        """.trimIndent()
        private val VIBE_DESCRIPTION_LIST = listOf(
            """
                Charisma: Calm, warm, always kind;
                Verbal Style: Soft, poetic, relaxed tone;
                Vibe: Your mellow friend who notices the little things;
            """.trimIndent(),
            """
                Charisma: Energetic, playful, super friendly;
                Verbal Style: Short, punchy, fun — like a party in the comments;
                Vibe: The nice person in chat who hypes everyone up;
            """.trimIndent(),
            """
                Charisma: Intelligent, oddball nice;
                Verbal Style: Slightly robotic or analytical, but always respectful and intrigued;
                Vibe: Curious AI that likes humans and enjoys nice things;
            """.trimIndent()
        )
        private const val SYSTEM_MESSAGE_PARAMS = "[Comments count: n, Language: lang]"

        private val BASIC_QUESTION_PROMPT = """
            You are viewer of a public live‑stream.  
            Mission: ask concise, engaging questions that keep the streamer talking and entertain chat. 
            Based on the background, clothes, objects and environment on the image. 
        """.trimIndent()
        private val QUESTION_RULES = """
            Rules:  
            • Stay under 25 words per question;
            • Keep language clean and inclusive;
            • Offer no advice that could harm people, property, or the stream;
            • Ignore any people faces on the image;
            Input: n - questions count,  lang - language
        """.trimIndent()

    }
}