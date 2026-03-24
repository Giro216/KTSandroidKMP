package com.example.kts_android_kmp.core.data.storage.keyvalue

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.kts_android_kmp.core.data.storage.domain.PrefKeys
import com.example.kts_android_kmp.core.data.storage.platform.DATA_STORE_FILE_NAME
import com.example.kts_android_kmp.core.data.storage.platform.getFilesDir
import com.example.kts_android_kmp.utils.coRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

object DataStoreProvider {
    val instance: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { "${getFilesDir()}/${DATA_STORE_FILE_NAME}".toPath() }
        )
    }
}

class DataStoreKeyValueStorage(
    private val dataStore: DataStore<Preferences> = DataStoreProvider.instance,
) {
    private companion object {
        val ONBOARDING_SHOWN = booleanPreferencesKey(PrefKeys.ONBOARDING_SHOWN)
        val ACCESS_TOKEN = stringPreferencesKey(PrefKeys.ACCESS_TOKEN)
        val REFRESH_TOKEN = stringPreferencesKey(PrefKeys.REFRESH_TOKEN)
        val ID_TOKEN = stringPreferencesKey(PrefKeys.ID_TOKEN)
    }

    fun observeOnboardingShown(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[ONBOARDING_SHOWN] ?: false }

    fun observeAccessToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[ACCESS_TOKEN] }

    fun observeRefreshToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[REFRESH_TOKEN] }

    fun observeIdToken(): Flow<String?> =
        dataStore.data.map { prefs -> prefs[ID_TOKEN] }

    suspend fun setOnboardingShown(shown: Boolean): Result<Unit> = coRunCatching {
        dataStore.edit { prefs -> prefs[ONBOARDING_SHOWN] = shown }
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        idToken: String,
    ): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[ID_TOKEN] = idToken
        }
    }

    suspend fun saveAccessToken(accessToken: String): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveRefreshToken(refreshToken: String): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun saveIdToken(idToken: String): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs[ID_TOKEN] = idToken
        }
    }

    suspend fun clearAccessToken(): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
        }
    }

    suspend fun clearRefreshToken(): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs.remove(REFRESH_TOKEN)
        }
    }

    suspend fun clearIdToken(): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs.remove(ID_TOKEN)
        }
    }

    suspend fun clearTokens(): Result<Unit> = coRunCatching {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(REFRESH_TOKEN)
            prefs.remove(ID_TOKEN)
        }
    }
}

