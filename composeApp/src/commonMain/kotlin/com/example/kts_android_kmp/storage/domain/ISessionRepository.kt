package com.example.kts_android_kmp.storage.domain

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel
import kotlinx.coroutines.flow.Flow

interface ISessionRepository {
    fun onboardingShown(): Flow<Boolean>
    suspend fun setOnboardingShown(shown: Boolean)

    suspend fun saveTokens(tokens: TokensModel)
    suspend fun clearTokens()

    fun isLoggedIn(): Flow<Boolean>
    fun accessToken(): Flow<String?>
}

