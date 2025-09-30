package com.bunbeauty.tiptoplive.common.util

import com.bunbeauty.tiptoplive.common.data.model.ApiResult
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class HttpResponseException(val code: Int) : Exception("Network call failed ($code)")

suspend inline fun <reified R> safeCall(
    crossinline networkCall: suspend () -> HttpResponse
): ApiResult<R> {
    return try {
        val response = networkCall()
        if (response.status.isSuccess()) {
            ApiResult.Success(data = response.body())
        } else {
            ApiResult.Error(
                throwable = HttpResponseException(
                    code = response.status.value
                )
            )
        }
    } catch (exception: ClientRequestException) {
        exception.printStackTrace()
        ApiResult.Error(throwable = exception)
    } catch (exception: Throwable) {
        exception.printStackTrace()
        ApiResult.Error(throwable = exception)
    }
}