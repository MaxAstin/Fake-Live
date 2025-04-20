package com.bunbeauty.tiptoplive.features.stream.data.repository

import android.content.Context
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.data.model.toResult
import com.bunbeauty.tiptoplive.features.stream.data.store.CommentStore
import com.bunbeauty.tiptoplive.features.stream.data.model.CommentsJson
import com.bunbeauty.tiptoplive.features.stream.data.mapper.toContent
import com.bunbeauty.tiptoplive.features.stream.domain.model.CommentType
import com.bunbeauty.tiptoplive.shared.data.OpenAiService
import com.bunbeauty.tiptoplive.shared.data.model.OpenAiResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val liveScreenRepository: LiveScreenRepository,
    private val openAiService: OpenAiService
) {

    private val resources = context.resources
    private val oneLetterComments = CommentStore(comments = resources.getStringArray(R.array.one_letter_comments))
    private val twoLetterComments = CommentStore(comments = resources.getStringArray(R.array.two_letter_comments))
    private val threeLetterComments = CommentStore(comments = resources.getStringArray(R.array.three_letter_comments))
    private val oneWordComments = CommentStore(comments = resources.getStringArray(R.array.one_word_comment))
    private val longComments = CommentStore(comments = resources.getStringArray(R.array.long_comments))
    private val questionComments = CommentStore(comments = resources.getStringArray(R.array.question_comments))

    fun getNextComment(type: CommentType): String {
        val comments = when (type) {
            CommentType.ONE_LETTER -> oneLetterComments
            CommentType.TWO_LETTER -> twoLetterComments
            CommentType.THREE_LETTER -> threeLetterComments
            CommentType.ONE_WORD -> oneWordComments
            CommentType.LONG -> longComments
            CommentType.QUESTION -> questionComments
        }
        return comments.getNext()
    }

    suspend fun getComments(count: Int): Result<List<String>> {
        return openAiService.getComments(
            count = count,
            language = Locale.getDefault().language,
            image = liveScreenRepository.liveScreen
        ).toResult()
            .fold(
                onSuccess = { success ->
                    success.toComments()
                },
                onFailure = { Result.failure(it) }
            )
    }

    private fun OpenAiResponse.toComments(): Result<List<String>> {
        return toContent()
            .mapCatching { content ->
                Json.decodeFromString<CommentsJson>(string = content).comments
            }
    }

}