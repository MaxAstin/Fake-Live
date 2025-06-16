package com.bunbeauty.tiptoplive.features.recording

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ScreenInfo(
    val widthPx: Int,
    val heightPx: Int,
    val topOffsetPx: Int,
    val bottomOffsetPx: Int
): Parcelable