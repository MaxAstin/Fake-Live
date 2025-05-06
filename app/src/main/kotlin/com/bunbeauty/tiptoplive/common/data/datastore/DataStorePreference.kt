package com.bunbeauty.tiptoplive.common.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

class DataStorePreference<T : Any>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
) {

    suspend fun set(value: T) {
        dataStore.edit { preferences -> preferences[key] = value }
    }

    suspend fun get(): T? {
        return nullableFlow().firstOrNull()
    }

    fun flow(defaultValue: T? = null): Flow<T> {
        return if (defaultValue == null) {
            nullableFlow().filterNotNull()
        } else {
            nullableFlow().map {
                it ?: defaultValue
            }
        }
    }

    private fun nullableFlow(): Flow<T?> {
        return dataStore.data.map { preferences -> preferences[key] }
    }

}

fun DataStore<Preferences>.string(name: String): DataStorePreference<String> {
    return DataStorePreference(
        dataStore = this,
        key = stringPreferencesKey(name)
    )
}

fun DataStore<Preferences>.boolean(name: String): DataStorePreference<Boolean> {
    return DataStorePreference(
        dataStore = this,
        key = booleanPreferencesKey(name)
    )
}