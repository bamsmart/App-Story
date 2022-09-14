package com.shinedev.digitalent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shinedev.digitalent.data.pref.AuthPreference
import com.shinedev.digitalent.view.login.LoginViewModel
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