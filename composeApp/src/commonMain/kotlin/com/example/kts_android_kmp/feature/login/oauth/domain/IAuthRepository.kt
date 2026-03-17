package com.example.kts_android_kmp.feature.login.oauth.domain

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel

interface IAuthRepository {
    suspend fun saveTokens(tokens: TokensModel)
    suspend fun logout()
}