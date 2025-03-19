package com.bunbeauty.tiptoplive.features.progress.domain.model

enum class Level(
    val number: Int,
    val points: Int
) {
    LEVEL_1(number = 1, points = 0),
    LEVEL_2(number = 2, points = 3),
    LEVEL_3(number = 3, points = 5),
    LEVEL_4(number = 4, points = 8),
    LEVEL_5(number = 5, points = 12),
    LEVEL_6(number = 6, points = 20),
    LEVEL_7(number = 7, points = 30),
    LEVEL_8(number = 8, points = 50),
    LEVEL_9(number = 9, points = 75),
    LEVEL_10(number = 10, points = 100)
}