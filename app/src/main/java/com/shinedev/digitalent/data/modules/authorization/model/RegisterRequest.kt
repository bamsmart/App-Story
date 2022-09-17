package com.shinedev.digitalent.data.modules.authorization.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)