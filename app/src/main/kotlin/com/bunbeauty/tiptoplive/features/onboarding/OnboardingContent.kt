package com.bunbeauty.tiptoplive.features.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bunbeauty.tiptoplive.R

data class OnboardingContent(
    @param:DrawableRes val imageId: Int,
    @param:StringRes val titleId: Int,
    @param:StringRes val textId: Int,
)

val onboardingContentList = listOf(
    OnboardingContent(
        imageId = R.drawable.img_onboarding_0,
        titleId = R.string.onboarding_go_live_title,
        textId = R.string.onboarding_go_live_text,
    ),
    OnboardingContent(
        imageId = R.drawable.img_onboarding_1,
        titleId = R.string.onboarding_attract_viewers_title,
        textId = R.string.onboarding_attract_viewers_text,
    ),
    OnboardingContent(
        imageId = R.drawable.img_onboarding_2,
        titleId = R.string.onboarding_get_likes_title,
        textId = R.string.onboarding_get_likes_text,
    ),
    OnboardingContent(
        imageId = R.drawable.img_onboarding_3,
        titleId = R.string.onboarding_interact_with_ai_title,
        textId = R.string.onboarding_interact_with_ai_text,
    ),
    OnboardingContent(
        imageId = R.drawable.img_onboarding_4,
        titleId = R.string.onboarding_pranks_title,
        textId = R.string.onboarding_pranks_text,
    ),
    OnboardingContent(
        imageId = R.drawable.img_onboarding_5,
        titleId = R.string.onboarding_answer_questions_title,
        textId = R.string.onboarding_answer_questions_text,
    ),
)