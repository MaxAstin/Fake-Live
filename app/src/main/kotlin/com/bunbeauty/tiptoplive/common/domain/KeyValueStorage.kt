package com.bunbeauty.tiptoplive.common.domain

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {

    suspend fun saveImageUri(uri: String)
    suspend fun saveUsername(username: String)
    suspend fun saveViewerCountIndex(index: Int)
    suspend fun saveRecording(isRecording: Boolean)
    suspend fun saveReviewProvided(provided: Boolean)
    suspend fun saveIsIntroViewed(isIntroViewed: Boolean)
    suspend fun saveLastUsedDate(date: String)
    suspend fun saveUsedDayCount(count: Int)
    suspend fun saveProgressPoints(points: Int)
    suspend fun saveShouldShowProgressHint(shouldShowProgressHint: Boolean)
    suspend fun saveNewLevel(newLevel: Boolean)
    suspend fun saveShowStreamDurationLimit(show: Boolean)
    suspend fun saveNotifiedOfStreamDurationLimit(notified: Boolean)
    suspend fun saveLastFeedbackDate(date: String)

    fun getImageUriFlow(): Flow<String?>
    suspend fun getUsername(): String?
    suspend fun getViewerCountIndex(defaultValue: Int): Int
    suspend fun getRecording(defaultValue: Boolean): Boolean
    suspend fun getReviewProvided(defaultValue: Boolean): Boolean
    suspend fun getIsIntroViewed(defaultValue: Boolean): Boolean
    suspend fun getLastUsedDate(): String?
    suspend fun getUsedDayCount(defaultValue: Int): Int
    suspend fun getProgressPoints(defaultValue: Int): Int
    suspend fun getShouldShowProgressHint(defaultValue: Boolean): Boolean
    suspend fun getNewLevel(defaultValue: Boolean): Boolean
    suspend fun getShowStreamDurationLimit(defaultValue: Boolean): Boolean
    suspend fun getNotifiedOfStreamDurationLimit(defaultValue: Boolean): Boolean
    suspend fun getLastFeedbackDate(): String?

}