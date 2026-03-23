package com.example.kts_android_kmp.feature.login.oauth.platform

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto

actual class TokenRefresher {
    actual suspend fun refreshToken(refreshToken: String): TokensModelDto? {
        return null
    }
}