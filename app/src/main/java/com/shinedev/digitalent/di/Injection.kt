package com.shinedev.digitalent.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthApiService
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthRepository
import com.shinedev.digitalent.data.local.room.StoryDatabase
import com.shinedev.digitalent.data.local.pref.AuthPreferenceDataStore
import com.shinedev.digitalent.data.modules.story.StoryApiService
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.data.remote.Retrofit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreferenceDataStore.AUTH_PREFERENCE)

    fun providePrefDataStore(context: Context): AuthPreferenceDataStore {
        return AuthPreferenceDataStore.getInstance(context.dataStore)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        // DataStore
        val pref = AuthPreferenceDataStore.getInstance(context.dataStore)
        val token: String = runBlocking { pref.getToken().first() }

        // Database
        val database = StoryDatabase.getInstance(context)
        val storyDao = database.storyDao()
        val remoteKeysDao = database.remoteKeysDao()

        // Api Service
        val apiService = Retrofit.createService(StoryApiService::class.java)
        return StoryRepository(token, storyDao, remoteKeysDao, apiService)
    }

    fun provideAuthRepository(context: Context): UserAuthRepository {
        val pref = AuthPreferenceDataStore.getInstance(context.dataStore)
        val apiService = Retrofit.createService(UserAuthApiService::class.java)
        return UserAuthRepository(pref, apiService)
    }

}