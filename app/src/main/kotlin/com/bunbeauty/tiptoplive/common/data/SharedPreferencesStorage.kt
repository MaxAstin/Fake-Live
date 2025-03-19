package com.bunbeauty.tiptoplive.common.data

import android.content.Context
import androidx.core.content.edit
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val IMAGE_URI_KEY = "image uri"
private const val USERNAME_KEY = "username"
private const val VIEWER_COUNT_INDEX_KEY = "viewer count index"
private const val SHOULD_ASK_FEEDBACK_KEY = "should ask feedback"
private const val SHOULD_HIGHLIGHT_DONATE_KEY = "should highlight donate"
private const val IS_INTRO_VIEWED_KEY = "is intro viewed"
private const val LAST_USED_DATE_KEY = "last used date"
private const val USED_DAY_COUNT_KEY = "used day count"
private const val PROGRESS_POINTS_KEY = "progress points"
private const val SHOULD_SHOW_PROGRESS_HINT_KEY = "should show progress hint"
private const val NEW_LEVEL_KEY = "new level"

class SharedPreferencesStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : KeyValueStorage {

    private val sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE)
    private val mutableImageUriFlow = MutableStateFlow(getImageUri())

    override suspend fun saveImageUri(uri: String) {
        mutableImageUriFlow.value = uri
        sharedPreferences.edit {
            putString(IMAGE_URI_KEY, uri)
        }
    }

    override suspend fun saveUsername(username: String) {
        sharedPreferences.edit {
            putString(USERNAME_KEY, username)
        }
    }

    override suspend fun saveViewerCountIndex(index: Int) {
        sharedPreferences.edit {
            putInt(VIEWER_COUNT_INDEX_KEY, index)
        }
    }

    override suspend fun saveShouldAskFeedback(shouldAsk: Boolean) {
        sharedPreferences.edit {
            putBoolean(SHOULD_ASK_FEEDBACK_KEY, shouldAsk)
        }
    }

    override suspend fun saveShouldHighlightDonate(shouldHighlight: Boolean) {
        sharedPreferences.edit {
            putBoolean(SHOULD_HIGHLIGHT_DONATE_KEY, shouldHighlight)
        }
    }

    override suspend fun saveIsIntroViewed(isIntroViewed: Boolean) {
        sharedPreferences.edit {
            putBoolean(IS_INTRO_VIEWED_KEY, isIntroViewed)
        }
    }

    override suspend fun saveLastUsedDate(date: String) {
        sharedPreferences.edit {
            putString(LAST_USED_DATE_KEY, date)
        }
    }

    override suspend fun saveUsedDayCount(count: Int) {
        sharedPreferences.edit {
            putInt(USED_DAY_COUNT_KEY, count)
        }
    }

    override suspend fun saveProgressPoints(points: Int) {
        sharedPreferences.edit {
            putInt(PROGRESS_POINTS_KEY, points)
        }
    }

    override suspend fun saveShouldShowProgressHint(shouldShowProgressHint: Boolean) {
        sharedPreferences.edit {
            putBoolean(SHOULD_SHOW_PROGRESS_HINT_KEY, shouldShowProgressHint)
        }
    }

    override suspend fun saveNewLevel(newLevel: Boolean) {
        sharedPreferences.edit {
            putBoolean(NEW_LEVEL_KEY, newLevel)
        }
    }

    override fun getImageUriFlow(): Flow<String?> {
        return mutableImageUriFlow.asStateFlow()
    }

    override suspend fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME_KEY, null)
    }

    override suspend fun getViewerCountIndex(defaultValue: Int): Int {
        return sharedPreferences.getInt(VIEWER_COUNT_INDEX_KEY, defaultValue)
    }

    override suspend fun getShouldAskFeedback(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(SHOULD_ASK_FEEDBACK_KEY, defaultValue)
    }

    override suspend fun getShouldHighlightDonate(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(SHOULD_HIGHLIGHT_DONATE_KEY, defaultValue)
    }

    override suspend fun getIsIntroViewed(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(IS_INTRO_VIEWED_KEY, defaultValue)
    }

    override suspend fun getLastUsedDate(): String? {
        return sharedPreferences.getString(LAST_USED_DATE_KEY, null)
    }

    override suspend fun getUsedDayCount(defaultValue: Int): Int {
        return sharedPreferences.getInt(USED_DAY_COUNT_KEY, defaultValue)
    }

    override suspend fun getProgressPoints(defaultValue: Int): Int {
        return sharedPreferences.getInt(PROGRESS_POINTS_KEY, defaultValue)
    }

    override suspend fun getShouldShowProgressHint(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(SHOULD_SHOW_PROGRESS_HINT_KEY, defaultValue)
    }

    override suspend fun getNewLevel(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(NEW_LEVEL_KEY, defaultValue)
    }

    private fun getImageUri(): String? {
        return sharedPreferences.getString(IMAGE_URI_KEY, null)
    }

}