package com.bunbeauty.tiptoplive.common.domain

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {

    suspend fun saveImageUri(uri: String)
    suspend fun saveUsername(username: String)
    suspend fun saveViewerCountIndex(index: Int)
    suspend fun saveShouldAskFeedback(shouldAsk: Boolean)
    suspend fun saveShouldHighlightDonate(shouldHighlight: Boolean)
    suspend fun saveIsIntroViewed(isIntroViewed: Boolean)
    suspend fun saveLastUsedDate(date: String)
    suspend fun saveUsedDayCount(count: Int)
    suspend fun saveProgressPoints(points: Int)
    suspend fun saveShouldShowProgressHint(shouldShowProgressHint: Boolean)
    suspend fun saveNewLevel(newLevel: Boolean)

    fun getImageUriFlow(): Flow<String?>
    suspend fun getUsername(): String?
    suspend fun getViewerCountIndex(defaultValue: Int): Int
    suspend fun getShouldAskFeedback(defaultValue: Boolean): Boolean
    suspend fun getShouldHighlightDonate(defaultValue: Boolean): Boolean
    suspend fun getIsIntroViewed(defaultValue: Boolean): Boolean
    suspend fun getLastUsedDate(): String?
    suspend fun getUsedDayCount(defaultValue: Int): Int
    suspend fun getProgressPoints(defaultValue: Int): Int
    suspend fun getShouldShowProgressHint(defaultValue: Boolean): Boolean
    suspend fun getNewLevel(defaultValue: Boolean): Boolean

}