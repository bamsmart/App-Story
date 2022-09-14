package com.shinedev.digitalent.domain.authorization

import com.shinedev.digitalent.data.login.LoginRequest
import com.shinedev.digitalent.data.login.LoginResponse
import com.shinedev.digitalent.network.BaseResponse
import com.shinedev.digitalent.data.register.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthApiService {
    @POST("register")
    fun register(
        @Body registerData: RegisterRequest
    ): Call<BaseResponse>

    @POST("login")
    fun login(
        @Body loginData: LoginRequest
    ): Call<LoginResponse>
}