package com.example.kts_android_kmp.feature.login.models

sealed interface LoginUiEvent {

    data object LoginSuccessEvent : LoginUiEvent

    data object LoginErrorEvent : LoginUiEvent
}