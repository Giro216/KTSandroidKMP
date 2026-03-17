package com.example.kts_android_kmp.feature.login.oauth.platform

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel

expect class AppAuthHandler {
    suspend fun performTokenRequest(): TokensModel?
}

