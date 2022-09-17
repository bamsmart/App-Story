package com.shinedev.digitalent.data.modules.authorization.model

data class LoginRequest(
    val email: String,
    val password: String
)