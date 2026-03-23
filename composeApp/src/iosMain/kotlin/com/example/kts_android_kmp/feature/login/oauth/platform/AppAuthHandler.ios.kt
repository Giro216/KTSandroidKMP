package com.example.kts_android_kmp.feature.login.oauth.platform

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto

actual class AppAuthHandler {
    actual suspend fun performTokenRequest(): TokensModelDto? {
        return null
    }
}