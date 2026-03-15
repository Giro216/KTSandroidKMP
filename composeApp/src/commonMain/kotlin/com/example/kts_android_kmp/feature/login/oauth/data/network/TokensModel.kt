package com.example.kts_android_kmp.feature.login.oauth.data.network

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
)