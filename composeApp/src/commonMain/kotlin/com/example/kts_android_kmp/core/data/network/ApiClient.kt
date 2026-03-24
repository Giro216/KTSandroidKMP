package com.example.kts_android_kmp.core.data.network

import com.example.kts_android_kmp.core.data.network.platform.KtorEngineFactory
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

object ApiClient {

    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    fun createHttpClient(
        tokenProvider: AuthTokenProvider,
    ): HttpClient {
        return HttpClient(KtorEngineFactory.create()) {
            expectSuccess = true

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = tokenProvider.accessTokenFlow().first().orEmpty().trim()
                        if (token.isBlank()) return@loadTokens null
                        BearerTokens(accessToken = token, refreshToken = "")
                    }

                    // Сейчас авто-рефреш токена не используем.
                    // Если появится refreshToken/use-case — можно реализовать здесь.
                    refreshTokens { null }
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.d(tag = "Ktor") { message }
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = ApiConfig.GITHUB_API_HOST
                }
                header(HttpHeaders.Accept, ContentType.Application.Json)
                contentType(ContentType.Application.Json)
                header(HttpHeaders.UserAgent, "KTS-android-KMP")
            }
        }
    }
}
