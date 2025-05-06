package com.bunbeauty.tiptoplive.common.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.bunbeauty.tiptoplive.common.domain.KeyValueObservableStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val NEW_AWARDS_KEY = "new awards"

class DataStoreStorage @Inject constructor(
    dataStore: DataStore<Preferences>
): KeyValueObservableStorage {

    private val newAwardsPreferences = dataStore.boolean(NEW_AWARDS_KEY)

    override fun getNewAwardsFlow(defaultValue: Boolean): Flow<Boolean> {
        return newAwardsPreferences.flow(defaultValue = defaultValue)
    }

    override suspend fun saveNewAwards(hasNewAwards: Boolean) {
        newAwardsPreferences.set(hasNewAwards)
    }

}