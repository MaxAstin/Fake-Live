package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.common.util.chance
import com.bunbeauty.tiptoplive.common.util.percent
import com.bunbeauty.tiptoplive.features.stream.data.comment.CommentRepository
import com.bunbeauty.tiptoplive.features.stream.data.user.UserRepository
import com.bunbeauty.tiptoplive.features.stream.domain.model.Comment
import com.bunbeauty.tiptoplive.shared.domain.GetViewerCountUseCase
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

private const val COMMENT_LIST_SIZE = 100

class GetCommentsUseCase @Inject constructor(
    private val getViewerCountUseCase: GetViewerCountUseCase,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val getRandomCommentText: GetRandomCommentTextUseCase,
    private val getRandomUsernameUseCase: GetRandomUsernameUseCase,
) {

    private val mediumViewerCount = listOf(
        ViewerCount.V_400_500,
        ViewerCount.V_1K_2K,
        ViewerCount.V_5K_10K
    )

    operator fun invoke(): Flow<List<Comment>> {
        return flow {
            val viewerCount = getViewerCountUseCase()

            while (true) {
                val comments = getComments()
                val chunkSize = getChunkSize(viewerCount = viewerCount)
                var currentIndex = 0

                while (currentIndex < comments.size) {
                    val chunkEnd = (currentIndex + chunkSize).coerceAtMost(comments.size)
                    emit(comments.slice(currentIndex until chunkEnd))
                    delay(getDelay(viewerCount = viewerCount))
                    currentIndex = chunkEnd + 1
                }
            }
        }
    }

    private fun getChunkSize(viewerCount: ViewerCount): Int {
        return when (viewerCount) {
            ViewerCount.V_100_200 -> 1
            in mediumViewerCount -> Random.nextInt(1, 3)
            else -> Random.nextInt(2, 4)
        }
    }

    private suspend fun getComments(): List<Comment> {
        return commentRepository.getComments(
            count = COMMENT_LIST_SIZE
        ).fold(
            onSuccess = { comments ->
                comments.shuffled()
                    .map { it.toComment() }
            },
            onFailure = { generateRandomComments() }
        )
    }

    private fun generateRandomComments(): List<Comment> {
        return List(COMMENT_LIST_SIZE) {
            getRandomCommentText().toComment()
        }
    }

    private fun String.toComment(): Comment {
        val picture = if (chance(10.percent)) {
            null
        } else {
            userRepository.getCommentPictureName()
        }

        return Comment(
            picture = picture,
            username = getRandomUsernameUseCase(),
            text = this,
        )
    }

    private fun getDelay(viewerCount: ViewerCount): Long {
        return when (viewerCount) {
            ViewerCount.V_100_200 -> Random.nextLong(1_600, 2_400)
            in mediumViewerCount -> Random.nextLong(1_200, 2_000)
            else -> Random.nextLong(800, 1_600)
        }
    }

}