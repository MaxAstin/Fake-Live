package com.bunbeauty.tiptoplive.features.progress.domain.model

enum class Level(
    val number: Int,
    val emoji: String,
    val points: Int
) {
    LEVEL_1(1, "\uD83D\uDC23", 0),
    LEVEL_2(2, "⭐", 3),
    LEVEL_3(3, "\uD83D\uDCAB", 5),
    LEVEL_4(4, "⚡", 8),
    LEVEL_5(5, "\uD83D\uDD25", 12),
    LEVEL_6(6, "\uD83D\uDE0E", 20),
    LEVEL_7(7, "\uD83E\uDD18", 30),
    LEVEL_8(8, "\uD83D\uDCAA", 50),
    LEVEL_9(9, "\uD83D\uDE80", 75),
    LEVEL_10(10, "\uD83D\uDC8E", 100)
}