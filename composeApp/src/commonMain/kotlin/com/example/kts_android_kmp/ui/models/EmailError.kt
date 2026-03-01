package com.example.kts_android_kmp.ui.models

sealed class EmailError {
    object Blank : EmailError()
    object Invalid : EmailError()
}

