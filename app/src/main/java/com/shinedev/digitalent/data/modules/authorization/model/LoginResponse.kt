package com.shinedev.digitalent.data.modules.authorization.model

import com.google.gson.annotations.SerializedName
import com.shinedev.digitalent.data.remote.BaseResponse

data class LoginResponse(
    @field:SerializedName("loginResult")
    val result: LoginResult? = null
) : BaseResponse()

data class LoginResult(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)