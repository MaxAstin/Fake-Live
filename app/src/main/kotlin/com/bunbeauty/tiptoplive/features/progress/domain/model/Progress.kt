package com.bunbeauty.tiptoplive.features.progress.domain.model

data class Progress(
    val showHint: Boolean,
    val level: Level,
    val progress: Float,
    val points: Int,
    val nextLevel: Level
)