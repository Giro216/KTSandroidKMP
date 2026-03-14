package com.example.kts_android_kmp.feature.login.oauth

import com.example.kts_android_kmp.feature.login.oauth.model.TokensModel

expect class AppAuthHandler {
    suspend fun performTokenRequest(): TokensModel?
}

