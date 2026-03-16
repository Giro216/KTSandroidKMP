package com.example.kts_android_kmp.network.platform

import io.ktor.client.engine.HttpClientEngine

internal expect object KtorEngineFactory {
    fun create(): HttpClientEngine
}

