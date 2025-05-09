package com.bunbeauty.tiptoplive.features.feedback.domain

import com.bunbeauty.tiptoplive.features.billing.domain.IsPremiumAvailableUseCase
import com.bunbeauty.tiptoplive.features.feedback.data.FeedbackRepository
import javax.inject.Inject

class SendFeedbackUseCase @Inject constructor(
    private val isPremiumAvailableUseCase: IsPremiumAvailableUseCase,
    private val feedbackRepository: FeedbackRepository
) {

    suspend operator fun invoke(feedback: String): Result<Unit> {
        val isPremium = isPremiumAvailableUseCase()
        return feedbackRepository.sendFeedback(
            feedback = feedback,
            isPremium = isPremium
        )
    }

}