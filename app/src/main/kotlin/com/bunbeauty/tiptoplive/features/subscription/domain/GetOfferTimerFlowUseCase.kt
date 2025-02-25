package com.bunbeauty.tiptoplive.features.subscription.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

class GetOfferTimerFlowUseCase @Inject constructor() {

    operator fun invoke(): Flow<String> {
        val now = LocalDateTime.now()
        val dayEnd = now.withHour(23)
            .withMinute(59)
            .withSecond(59)
        val untilDayEnd = now.until(dayEnd, ChronoUnit.HOURS)
        val offerEnd = if (untilDayEnd < 1) {
            dayEnd.plusHours(1)
        } else {
            dayEnd
        }
        var untilOfferFinished = Duration.between(now, offerEnd)
        var previousUpdateMillis: Long

        return flow {
            while (untilOfferFinished > Duration.ZERO) {
                previousUpdateMillis = LocalTime.now().nano / 1_000_000L
                emit(untilOfferFinished.format())
                untilOfferFinished = untilOfferFinished.minusSeconds(1)
                delay(1_000 - previousUpdateMillis)
            }
        }
    }

    private fun Duration.format(): String {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            toHours(),
            toMinutes() % 60,
            seconds % 60
        )
    }

}