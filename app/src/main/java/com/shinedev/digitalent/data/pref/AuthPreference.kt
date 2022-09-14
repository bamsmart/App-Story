package com.shinedev.digitalent.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getIsLogin(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[IS_LOGIN_KEY] ?: false
        }
    }

    suspend fun setIsLogin(isLogin: Boolean) {
        dataStore.edit { pref ->
            pref[IS_LOGIN_KEY] = isLogin
        }
    }

    suspend fun clear() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: AuthPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreference(dataStore)
                INSTANCE = instance
                instance
            }

        }

        const val AUTH_PREFERENCE = "auth_pref"
    }
}