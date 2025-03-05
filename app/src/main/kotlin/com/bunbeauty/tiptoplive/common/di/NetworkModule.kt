package com.bunbeauty.tiptoplive.common.di

import android.util.Log
import com.bunbeauty.tiptoplive.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
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
            install(Logging) {
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                this.host = "api.openai.com/v1/chat"
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                bearerAuth(BuildConfig.AUTH_TOKEN)
                Log.d("testTag", "token ${BuildConfig.AUTH_TOKEN}")

                url {
                    protocol = URLProtocol.HTTPS
                }
            }
        }
    }

}