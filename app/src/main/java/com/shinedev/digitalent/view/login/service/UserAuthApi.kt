package com.shinedev.digitalent.view.login.service

import com.shinedev.digitalent.network.BaseResponse
import com.shinedev.digitalent.view.login.LoginResponse
import com.shinedev.digitalent.view.register.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthApi {
    @POST("register")
    fun register(
        @Body registerData: RegisterRequest
    ): Call<BaseResponse>

    @POST("login")
    fun login(
        @Body loginData: LoginRequest
    ): Call<LoginResponse>
}