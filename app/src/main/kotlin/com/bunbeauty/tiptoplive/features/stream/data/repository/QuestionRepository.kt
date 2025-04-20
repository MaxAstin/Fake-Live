package com.bunbeauty.tiptoplive.features.stream.data.repository

import com.bunbeauty.tiptoplive.common.data.model.toResult
import com.bunbeauty.tiptoplive.features.stream.data.model.QuestionsJson
import com.bunbeauty.tiptoplive.features.stream.data.mapper.toContent
import com.bunbeauty.tiptoplive.shared.data.OpenAiService
import com.bunbeauty.tiptoplive.shared.data.model.OpenAiResponse
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

private const val QUESTION_COUNT = 10

@Singleton
class QuestionRepository @Inject constructor(
    private val openAiService: OpenAiService,
    private val liveScreenRepository: LiveScreenRepository
) {

    private val cache = ArrayDeque<String>()

    suspend fun getAiQuestion(): String? {
        if (cache.isEmpty()) {
            getRemoteQuestions(count = QUESTION_COUNT)
                .onSuccess { questions ->
                    cache.addAll(questions)
                }
        }

        return cache.removeFirstOrNull()
    }

    private suspend fun getRemoteQuestions(count: Int): Result<List<String>> {
        return openAiService.getQuestions(
            count = count,
            language = Locale.getDefault().language,
            image = liveScreenRepository.liveScreen
        ).toResult()
            .fold(
                onSuccess = { success ->
                    success.toQuestions()
                },
                onFailure = { Result.failure(it) }
            )
    }

    private fun OpenAiResponse.toQuestions(): Result<List<String>> {
        return toContent()
            .mapCatching { content ->
                Json.decodeFromString<QuestionsJson>(string = content).questions
            }
    }

}