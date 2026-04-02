package com.github_explorer.kts_android_kmp.core.data.storage.domain

import com.github_explorer.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun onboardingShown(): Flow<Boolean>
    suspend fun setOnboardingShown(shown: Boolean): Result<Unit>

    suspend fun saveTokens(tokens: TokensModelDto): Result<Unit>
    suspend fun clearTokens(): Result<Unit>

    fun isLoggedIn(): Flow<Boolean>
    fun accessToken(): Flow<String?>
}

