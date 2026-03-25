package com.github_explorer.kts_android_kmp.feature.login.oauth.data.network

object AuthConfig {
    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val END_SESSION_URI = "https://github.com/logout"
    const val SCOPE = "user,repo"

    const val CLIENT_ID = AuthSecrets.CLIENT_ID
    const val CLIENT_SECRET = AuthSecrets.CLIENT_SECRET

    const val CALLBACK_URL = "ru.kts.giro216.oauth://github.com/callback"
    const val LOGOUT_CALLBACK_URL = "ru.kts.oauth://github.com/logout_callback"
}