package com.example.kts_android_kmp.core.data.network.platform

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

actual fun initNapier() {
    Napier.base(DebugAntilog())
}

