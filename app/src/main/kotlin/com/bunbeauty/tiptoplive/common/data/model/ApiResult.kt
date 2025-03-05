package com.bunbeauty.tiptoplive.common.data.model

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val throwable: Throwable) : ApiResult<T>()
}

fun <T> ApiResult<T>.toResult(): Result<T> {
    return when (this) {
        is ApiResult.Success -> Result.success(data)
        is ApiResult.Error -> Result.failure(throwable)
    }
}