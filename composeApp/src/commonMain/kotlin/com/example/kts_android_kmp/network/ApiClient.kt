package com.example.kts_android_kmp.network

import com.example.kts_android_kmp.network.platform.KtorEngineFactory
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.Json

object ApiClient {

    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    fun createHttpClient(
        tokenProvider: AuthTokenProvider,
        scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
    ): HttpClient {
        var latestAccessToken: String? = null

        tokenProvider.accessTokenFlow()
            .onEach { latestAccessToken = it }
            .launchIn(scope)

        return HttpClient(KtorEngineFactory.create()) {
            expectSuccess = true

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

                latestAccessToken?.takeIf { it.isNotBlank() }?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}
