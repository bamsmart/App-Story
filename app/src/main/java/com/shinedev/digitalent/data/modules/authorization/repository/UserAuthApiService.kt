package com.shinedev.digitalent.data.modules.authorization.repository

import com.shinedev.digitalent.data.modules.authorization.model.LoginRequest
import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.authorization.model.RegisterRequest
import com.shinedev.digitalent.data.remote.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthApiService {
    @POST("register")
    suspend fun register(
        @Body registerData: RegisterRequest
    ): Result<BaseResponse>

    @POST("login")
    suspend fun login(
        @Body loginData: LoginRequest
    ): Result<LoginResponse>
}