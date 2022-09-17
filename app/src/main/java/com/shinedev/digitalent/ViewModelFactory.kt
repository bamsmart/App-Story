package com.shinedev.digitalent

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shinedev.digitalent.di.Injection
import com.shinedev.digitalent.ui.login.LoginViewModel
import com.shinedev.digitalent.ui.story.StoryViewModel
import com.shinedev.digitalent.ui.maps.MapsViewModel
import com.shinedev.digitalent.ui.register.RegisterViewModel
import com.shinedev.digitalent.ui.upload.UploadStoryViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(Injection.provideAuthRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(Injection.provideAuthRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(
                Injection.providePrefDataStore(context),
                Injection.provideStoryRepository(context)
            ) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(Injection.provideStoryRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(UploadStoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UploadStoryViewModel(Injection.provideStoryRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}