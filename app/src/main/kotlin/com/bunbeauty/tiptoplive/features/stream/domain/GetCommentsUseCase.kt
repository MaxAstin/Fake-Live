package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.common.util.chance
import com.bunbeauty.tiptoplive.common.util.percent
import com.bunbeauty.tiptoplive.features.stream.data.comment.CommentRepository
import com.bunbeauty.tiptoplive.features.stream.data.user.UserRepository
import com.bunbeauty.tiptoplive.features.stream.domain.model.Comment
import com.bunbeauty.tiptoplive.shared.domain.GetViewerCountUseCase
import com.bunbeauty.tiptoplive.shared.domain.model.ViewerCount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

private const val COMMENT_LIST_SIZE = 100
private const val PREFETCH_THRESHOLD = 20
private const val THRESHOLD_CHECK_PERIOD = 1_000L

@OptIn(ExperimentalCoroutinesApi::class)
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
        return channelFlow {
            val viewerCount = getViewerCountUseCase()
            val aiCommentChannel = Channel<String>(Channel.UNLIMITED)
            val aiCommentSize = MutableStateFlow(0)

            launch {
                while (isActive) {
                    delay(getDelay(viewerCount = viewerCount))

                    val chunkSize = getChunkSize(viewerCount = viewerCount)
                    val comments = List(chunkSize) {
                        val aiGenerated = !aiCommentChannel.isEmpty && chance(60.percent)
                        val commentText = if (aiGenerated) {
                            aiCommentSize.update { size -> size - 1 }
                            aiCommentChannel.receive()
                        } else {
                            getRandomCommentText()
                        }
                        commentText.toComment(aiGenerated = aiGenerated)
                    }
                    send(comments)
                }
            }
            launch {
                while (isActive) {
                    delay(THRESHOLD_CHECK_PERIOD)

                    if (aiCommentSize.value < PREFETCH_THRESHOLD) {
                        commentRepository.getComments(
                            count = COMMENT_LIST_SIZE
                        ).onSuccess { comments ->
                            comments.shuffled()
                                .forEach { aiCommentText ->
                                    aiCommentChannel.send(aiCommentText)
                                }
                            aiCommentSize.update { size ->
                                size + comments.size
                            }
                        }
                    }
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

    private fun String.toComment(aiGenerated: Boolean): Comment {
        val picture = if (chance(10.percent)) {
            null
        } else {
            userRepository.getCommentPictureName()
        }

        return Comment(
            uuid = UUID.randomUUID().toString(),
            picture = picture,
            username = getRandomUsernameUseCase(),
            text = this,
            aiGenerated = aiGenerated
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