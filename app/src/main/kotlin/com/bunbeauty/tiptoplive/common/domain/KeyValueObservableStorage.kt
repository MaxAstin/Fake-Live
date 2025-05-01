package com.bunbeauty.tiptoplive.common.domain

import kotlinx.coroutines.flow.Flow

interface KeyValueObservableStorage {

    fun getNewAwardsFlow(defaultValue: Boolean): Flow<Boolean>

    suspend fun saveNewAwards(hasNewAwards: Boolean)

}