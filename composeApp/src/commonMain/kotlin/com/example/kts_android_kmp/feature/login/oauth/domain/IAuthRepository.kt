package com.example.kts_android_kmp.feature.login.oauth.domain

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto

interface IAuthRepository {
    suspend fun saveTokens(tokens: TokensModelDto): Result<Unit>
    suspend fun logout(): Result<Unit>
}