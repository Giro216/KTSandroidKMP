package com.example.kts_android_kmp.feature.login.oauth.platform

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel

actual class AppAuthHandler {
    actual suspend fun performTokenRequest(): TokensModel? { return null}
}