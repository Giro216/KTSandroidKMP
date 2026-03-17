package com.example.kts_android_kmp.feature.profile.domain

interface IProfileRepository {
    suspend fun loadProfile(): Result<UserProfileEntity>
}
