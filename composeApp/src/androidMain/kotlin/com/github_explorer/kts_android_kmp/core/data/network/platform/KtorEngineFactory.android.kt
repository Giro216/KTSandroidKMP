package com.github_explorer.kts_android_kmp.core.data.network.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual object KtorEngineFactory {
    actual fun create(): HttpClientEngine = OkHttp.create()
}

