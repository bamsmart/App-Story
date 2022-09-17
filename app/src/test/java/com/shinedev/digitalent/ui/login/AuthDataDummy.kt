package com.shinedev.digitalent.ui.login

import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.authorization.model.LoginResult
import com.shinedev.digitalent.data.remote.BaseResponse

object AuthDataDummy {
    fun generateLoginResponse(): LoginResponse {
        return LoginResponse(
            result = LoginResult(
                userId = "yJ1c2",
                name = "Bambang T. Lasiman",
                token = "eyJ1c2VySWQiOiJ1c2VyLVc2QUM4YUdVc"
            )
        )
    }

    fun generateRegisterResponse(): BaseResponse {
        return BaseResponse(
            error = false,
            message = "success"
        )
    }
}