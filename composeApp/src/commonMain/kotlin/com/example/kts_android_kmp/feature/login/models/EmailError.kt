package com.example.kts_android_kmp.feature.login.models

sealed class EmailError {
    object Blank : EmailError()
    object Invalid : EmailError()
}

