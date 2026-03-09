package com.example.kts_android_kmp.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual object KtorEngineFactory {
    actual fun create(): HttpClientEngine = Darwin.create()
}

