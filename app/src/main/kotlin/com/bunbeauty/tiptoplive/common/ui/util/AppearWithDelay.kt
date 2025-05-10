package com.bunbeauty.tiptoplive.common.ui.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun AppearWithDelay(
    delayTimeMillis: Long = 2_000,
    content: @Composable () -> Unit
) {
    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(timeMillis = delayTimeMillis)
        visible = true
    }

    AnimatedVisibility(visible = visible) {
        content()
    }
}