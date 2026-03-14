package com.example.kts_android_kmp.feature.login.oauth

import com.example.kts_android_kmp.feature.login.oauth.model.TokensModel

actual class AppAuthHandler {
    actual suspend fun performTokenRequest(): TokensModel? { return null}
}