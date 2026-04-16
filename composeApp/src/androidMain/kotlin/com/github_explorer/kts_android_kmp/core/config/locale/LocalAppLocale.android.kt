package com.github_explorer.kts_android_kmp.core.config.locale

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

actual object LocalAppLocale {

    private var startupLocaleTag: String? = null
    private var appliedLocaleTag: String? = null

    actual val current: String
        @Composable
        get() = Locale.getDefault().toLanguageTag()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val context = LocalContext.current

        if (startupLocaleTag == null) {
            startupLocaleTag = getDefaultLocale()
        }

        val targetTag = value?.takeIf { it.isNotBlank() } ?: startupLocaleTag!!
        val parsed = Locale.forLanguageTag(targetTag)
        val localeToApply = if (parsed == Locale.ROOT) {
            Locale.forLanguageTag(startupLocaleTag!!)
        } else {
            parsed
        }
        val localeTagToApply = localeToApply.toLanguageTag()

        if (appliedLocaleTag != localeTagToApply) {
            Locale.setDefault(localeToApply)
            appliedLocaleTag = localeTagToApply
        }

        val localizedConfiguration = Configuration(context.resources.configuration)
        localizedConfiguration.setLocale(localeToApply)
        val newContext = context.createConfigurationContext(localizedConfiguration)

        return LocalContext provides newContext
    }
}