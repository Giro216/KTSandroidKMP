package com.example.kts_android_kmp.feature.login.oauth.data.repo

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokenStorage
import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel
import com.example.kts_android_kmp.feature.login.oauth.domain.IAuthRepository

class AuthRepository : IAuthRepository {
    override fun saveTokens(tokens: TokensModel) {
        TokenStorage.accessToken = tokens.accessToken
        TokenStorage.refreshToken = tokens.refreshToken
        TokenStorage.idToken = tokens.idToken
    }

    override fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null
    }
}