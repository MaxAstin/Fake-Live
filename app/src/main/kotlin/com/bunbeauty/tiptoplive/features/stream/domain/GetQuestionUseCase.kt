package com.bunbeauty.tiptoplive.features.stream.domain

import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.stream.data.repository.QuestionRepository
import com.bunbeauty.tiptoplive.features.stream.data.repository.UserRepository
import com.bunbeauty.tiptoplive.features.stream.domain.model.CommentType
import com.bunbeauty.tiptoplive.features.stream.domain.model.Question
import java.util.UUID
import javax.inject.Inject

class GetQuestionUseCase @Inject constructor(
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
    private val getRandomCommentText: GetRandomCommentTextUseCase,
    private val getRandomUsernameUseCase: GetRandomUsernameUseCase,
) {

    suspend operator fun invoke(): Question {
        val questionText = if (isPremiumAvailableUseCase()) {
            questionRepository.getAiQuestion() ?: getRandomCommentText(type = CommentType.QUESTION)
        } else {
            getRandomCommentText(type = CommentType.QUESTION)
        }

        return  Question(
            uuid = UUID.randomUUID().toString(),
            avatarName = userRepository.getQuestionAvatarName(),
            username = getRandomUsernameUseCase(),
            text = questionText
        )
    }

}