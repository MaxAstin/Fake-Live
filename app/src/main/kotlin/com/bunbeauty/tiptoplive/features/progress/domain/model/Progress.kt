package com.bunbeauty.tiptoplive.features.progress.domain.model

data class Progress(
    val showHint: Boolean,
    val level: Int,
    val emoji: String,
    val progress: Float,
    val points: Int,
    val nextLevelPoints: Int
)