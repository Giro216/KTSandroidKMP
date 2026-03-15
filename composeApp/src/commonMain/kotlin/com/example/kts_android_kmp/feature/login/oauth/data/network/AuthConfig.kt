package com.example.kts_android_kmp.feature.login.oauth.data.network

object AuthConfig {
    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val END_SESSION_URI = "https://github.com/logout"
    const val SCOPE = "user,repo"

    const val CLIENT_ID = "Ov23lifEwVtjNUs2khSP"
    const val CLIENT_SECRET = "c8acf6114d38ed324439a1b541e3e82864be3be1"
    const val CALLBACK_URL = "ru.kts.giro216.oauth://github.com/callback"
    const val LOGOUT_CALLBACK_URL = "ru.kts.oauth://github.com/logout_callback"
}