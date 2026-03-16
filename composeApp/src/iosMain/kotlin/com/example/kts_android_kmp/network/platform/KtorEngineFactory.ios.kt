package com.example.kts_android_kmp.network.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual object KtorEngineFactory {
    actual fun create(): HttpClientEngine = Darwin.create()
}

