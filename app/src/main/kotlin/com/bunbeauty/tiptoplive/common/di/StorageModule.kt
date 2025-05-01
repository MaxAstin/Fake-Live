package com.bunbeauty.tiptoplive.common.di

import com.bunbeauty.tiptoplive.common.data.SharedPreferencesStorage
import com.bunbeauty.tiptoplive.common.data.datastore.DataStoreStorage
import com.bunbeauty.tiptoplive.common.domain.KeyValueObservableStorage
import com.bunbeauty.tiptoplive.common.domain.KeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StorageModule {

    @Singleton
    @Binds
    fun bindsKeyValueStorage(sharedPreferencesStorage: SharedPreferencesStorage): KeyValueStorage

    @Singleton
    @Binds
    fun bindsKeyValueObservableStorage(dataStoreStorage: DataStoreStorage): KeyValueObservableStorage

}