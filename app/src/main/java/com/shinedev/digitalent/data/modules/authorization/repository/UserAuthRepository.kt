package com.shinedev.digitalent.data.modules.authorization.repository

import com.shinedev.digitalent.common.wrapEspressoIdlingResource
import com.shinedev.digitalent.data.local.pref.AuthPreferenceDataStore
import com.shinedev.digitalent.data.modules.authorization.model.LoginRequest
import com.shinedev.digitalent.data.modules.authorization.model.RegisterRequest

class UserAuthRepository(
    private val pref: AuthPreferenceDataStore,
    private val apiService: UserAuthApiService
) {
    suspend fun setToken(token: String) {
        pref.setToken(token)
    }

    suspend fun setIsLogin(isLogin: Boolean) {
        pref.setIsLogin(isLogin)
    }

    suspend fun login(request: LoginRequest) =
        wrapEspressoIdlingResource {
            apiService.login(request)
        }

    suspend fun register(request: RegisterRequest) =
        wrapEspressoIdlingResource { apiService.register(request) }

}