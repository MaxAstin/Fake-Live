package com.bunbeauty.tiptoplive.features.stream.data.comment

import android.content.Context
import android.util.Base64
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.data.NetworkService
import com.bunbeauty.tiptoplive.common.data.model.CommentsJson
import com.bunbeauty.tiptoplive.common.data.model.CommentsResponse
import com.bunbeauty.tiptoplive.common.data.model.toResult
import com.bunbeauty.tiptoplive.features.stream.domain.CommentType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkService: NetworkService
) {

    private val resources = context.resources
    private val oneLetterComments = CommentStore(comments = resources.getStringArray(R.array.one_letter_comments))
    private val twoLetterComments = CommentStore(comments = resources.getStringArray(R.array.two_letter_comments))
    private val threeLetterComments = CommentStore(comments = resources.getStringArray(R.array.three_letter_comments))
    private val oneWordComments = CommentStore(comments = resources.getStringArray(R.array.one_word_comment))
    private val longComments = CommentStore(comments = resources.getStringArray(R.array.long_comments))
    private val questionComments = CommentStore(comments = resources.getStringArray(R.array.question_comments))

    private var currentPicture: ByteArray? = null

    fun updateCurrentPicture(bytes: ByteArray) {
        currentPicture = bytes
    }

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
        return networkService.getComments(
            count = count,
            language = Locale.getDefault().language,
            currentPicture = currentPicture?.toBase64()
        ).toResult()
            .fold(
                onSuccess = { success ->
                    success.toComments()
                },
                onFailure = { Result.failure(it) }
            )
    }

    private fun CommentsResponse.toComments(): Result<List<String>> {
        when (this ) {
            is CommentsResponse.Success -> {
                val content = this.choices.last {  choice ->
                    choice.message.role == "assistant"
                }.message.content

                return runCatching {
                    Json.decodeFromString<CommentsJson>(content).comments
                }
            }
            is CommentsResponse.Error -> {
                return Result.failure(Exception(error.message))
            }
        }
    }

    private fun ByteArray.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        outputStream.write(this)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

}