package com.example.kts_android_kmp.feature.login.oauth

import com.example.kts_android_kmp.feature.login.oauth.model.TokensModel

class AuthRepository {
    fun saveTokens(tokens: TokensModel) {
        TokenStorage.accessToken = tokens.accessToken
        TokenStorage.refreshToken = tokens.refreshToken
        TokenStorage.idToken = tokens.idToken
    }

    fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null
    }
}

