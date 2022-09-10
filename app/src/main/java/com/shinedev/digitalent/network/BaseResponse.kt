package com.shinedev.digitalent.network

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @field:SerializedName("error")
    val error: Boolean = true,

    @field:SerializedName("message")
    val message: String = ""
)