package com.shinedev.digitalent.data.remote

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @field:SerializedName("error")
    open val error: Boolean = true,

    @field:SerializedName("message")
    open val message: String = ""
)