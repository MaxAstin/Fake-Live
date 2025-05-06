package com.bunbeauty.tiptoplive.features.feedback.data

import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

private const val MESSAGES_REFERENCE = "messages"

class FeedbackRepository @Inject constructor() {

    suspend fun sendFeedback(
        feedback: String,
        isPremium: Boolean
    ): Result<Unit> {
        val messagesReference = Firebase.database.getReference(MESSAGES_REFERENCE)
        return runCatching {
            val snapshot = messagesReference.get().await()
            val nextId = snapshot.childrenCount
            messagesReference.child(nextId.toString())
                .setValue(
                    FeedbackJson(
                        text = feedback,
                        language = Locale.getDefault().language,
                        isPremium = isPremium
                    )
                ).await()
        }
    }

}