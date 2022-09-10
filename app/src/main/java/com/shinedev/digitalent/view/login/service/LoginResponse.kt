package com.shinedev.digitalent.view.login

import com.google.gson.annotations.SerializedName
import com.shinedev.digitalent.network.BaseResponse

data class LoginResponse(
    @field:SerializedName("loginResult")
    val result: LoginResult
) : BaseResponse()

data class LoginResult(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)