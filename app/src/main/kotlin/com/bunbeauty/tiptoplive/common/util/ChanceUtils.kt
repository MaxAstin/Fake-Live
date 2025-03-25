package com.bunbeauty.tiptoplive.common.util

import kotlin.random.Random

data class Percent(val value: Int)

val Int.percent: Percent
    get() {
        return Percent(this).apply {
            validate()
        }
    }

fun chance(percent: Percent): Boolean {
    percent.validate()
    return Random.nextInt(0, 100) < percent.value
}

fun <T> chance(
    case1: Pair<Percent, T>,
    case2: Pair<Percent, T>? = null,
    case3: Pair<Percent, T>? = null,
    case4: Pair<Percent, T>? = null,
): T {
    val value = Random.nextInt(0, 100)
    val cases = listOfNotNull(case1, case2, case3, case4)

    val sum = cases.sumOf { it.first.value }
    if (sum != 100) {
        error("The sum of cases cannot but equal 100 ($sum)")
    }

    var percentSum = 0
    cases.forEach { (percent, result) ->
        percentSum += percent.value
        if (value < percentSum) {
            return result
        }
    }

    error("Correct case not found")
}

private fun Percent.validate() {
    if (value < 1 || value > 99) error("Chance must be between 1 and 99")
}