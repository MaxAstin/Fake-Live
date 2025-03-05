package com.bunbeauty.tiptoplive.common.util

import com.bunbeauty.tiptoplive.common.data.model.ApiResult
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified R> safeCall(
    crossinline networkCall: suspend () -> HttpResponse
): ApiResult<R> {
    return try {
        val response = networkCall()
        ApiResult.Success(data = response.body())
    } catch (exception: ClientRequestException) {
        exception.printStackTrace()
        ApiResult.Error(throwable = exception)
    } catch (exception: Exception) {
        exception.printStackTrace()
        ApiResult.Error(throwable = exception)
    }
}