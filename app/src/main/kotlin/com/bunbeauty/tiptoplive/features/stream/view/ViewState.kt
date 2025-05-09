package com.bunbeauty.tiptoplive.features.stream.view

import androidx.compose.runtime.Immutable
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionState
import com.bunbeauty.tiptoplive.features.stream.view.ui.QuestionUi
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ViewState(
    val image: ImageSource<*>,
    val username: String,
    val viewersCount: ViewersCountUi,
    val comments: ImmutableList<CommentUi>,
    val reactionCount: Int,
    val mode: Mode,
    val isCameraEnabled: Boolean,
    val isCameraFront: Boolean,
    val showJoinRequests: Boolean,
    val showInvite: Boolean,
    val questionState: QuestionState,
    val unreadQuestionCount: Int?,
    val selectedQuestion: QuestionUi?,
    val showDirect: Boolean,
)

enum class Mode {
    CAMERA,
    VIDEO
}

@Immutable
sealed interface ViewersCountUi {

    @Immutable
    data class UpToThousand(val count: String): ViewersCountUi

    @Immutable
    data class Thousands(
        val thousands: String,
        val hundreds: String,
    ): ViewersCountUi

}

@Immutable
data class CommentUi(
    val uuid: String,
    val picture: ImageSource<*>,
    val username: String,
    val text: String,
)