package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.features.stream.data.user.UserRepository
import com.bunbeauty.tiptoplive.features.stream.domain.model.Comment
import javax.inject.Inject
import kotlin.random.Random

class GetCommentsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val getRandomCommentText: GetRandomCommentTextUseCase,
    private val getRandomUsernameUseCase: GetRandomUsernameUseCase,
) {

    operator fun invoke(viewersCount: Int): List<Comment> {
        val count = if (viewersCount < 1000) {
            1
        } else {
            val maxCommentCount = (viewersCount / 1_000).coerceIn(2, 5)
            Random.nextInt(1, maxCommentCount)
        }

        return List(count) {
            val picture = if (Random.nextInt(10) == 0) {
                null
            } else {
                userRepository.getCommentPictureName()
            }
            Comment(
                picture = picture,
                username = getRandomUsernameUseCase(),
                text = getRandomCommentText(),
            )
        }
    }

}