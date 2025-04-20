package com.bunbeauty.tiptoplive.common.di

import android.util.Log
import com.bunbeauty.tiptoplive.BuildConfig
import com.bunbeauty.tiptoplive.features.stream.data.model.CommentsProperties
import com.bunbeauty.tiptoplive.features.stream.data.model.QuestionProperties
import com.bunbeauty.tiptoplive.shared.data.model.Properties
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun providesHttpClient(): HttpClient {
        return HttpClient(OkHttp.create()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        classDiscriminatorMode = ClassDiscriminatorMode.NONE
                        serializersModule = SerializersModule {
                            polymorphic(Properties::class, CommentsProperties::class, CommentsProperties.serializer())
                            polymorphic(Properties::class, QuestionProperties::class, QuestionProperties.serializer())
                        }
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20_000
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 20_000
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                exponentialDelay()
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