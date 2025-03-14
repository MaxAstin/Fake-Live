package com.bunbeauty.tiptoplive.common.di

import android.util.Log
import com.bunbeauty.tiptoplive.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient(OkHttp.create()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        classDiscriminator = "contentType"
                    }
                )
            }
            install(HttpRequestRetry) {
                retryOnException(
                    maxRetries = 3,
                    retryOnTimeout = true
                )
            }
            if (BuildConfig.DEBUG) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d("Ktor", message)
                        }
                    }
                }
            }
            install(DefaultRequest) {
                this.host = "api.openai.com/v1/chat"
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                bearerAuth(BuildConfig.AUTH_TOKEN)

                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
    }

}