package com.example.kts_android_kmp.feature.login.oauth

import com.example.kts_android_kmp.feature.login.oauth.model.TokensModel

actual class TokenRefresher {
    actual suspend fun refreshToken(refreshToken: String): TokensModel? {
        return null
    }
}