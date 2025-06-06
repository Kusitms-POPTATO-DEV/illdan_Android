package com.poptato.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

private val Context.dataStore by preferencesDataStore(name = "poptato_prefs")

class PoptatoDataStore(context: Context) {
    private val dataStore = context.dataStore

    val accessToken: Flow<String?> = dataStore.data.map { preferences -> preferences[ACCESS_TOKEN_KEY] }
    val refreshToken: Flow<String?> = dataStore.data.map { preferences -> preferences[REFRESH_TOKEN_KEY] }
    val deadlineDateMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DEADLINE_DATE_MODE] ?: false
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
            Timber.i("Access Token", token)
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
            Timber.i("Refresh Token", token)
        }
    }

    suspend fun setDeadlineDateMode(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[DEADLINE_DATE_MODE] = value
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val DEADLINE_DATE_MODE = booleanPreferencesKey("deadline_date_mode")
    }
}