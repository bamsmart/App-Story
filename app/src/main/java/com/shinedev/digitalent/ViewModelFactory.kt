package com.shinedev.digitalent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.detail.DetailStoryViewModel
import com.shinedev.digitalent.view.login.LoginViewModel
import com.shinedev.digitalent.view.register.RegisterViewModel
import com.shinedev.digitalent.view.upload.UploadStoryViewModel

class ViewModelWithPrefFactory(private val pref: AuthPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}