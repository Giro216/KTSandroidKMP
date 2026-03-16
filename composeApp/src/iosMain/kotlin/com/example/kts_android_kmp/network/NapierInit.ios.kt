package com.example.kts_android_kmp.network

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

actual fun initNapier() {
    Napier.base(DebugAntilog())
}

