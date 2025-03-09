package com.bunbeauty.tiptoplive.common.util

import kotlin.random.Random

data class Percent(val value: Int)

val Int.percent: Percent
    get() = Percent(this)

fun chance(percent: Percent): Boolean {
    if (percent.value < 1 || percent.value > 99) error("Chance must be between 1 and 99")

    return Random.nextInt(0, 100) < percent.value
}