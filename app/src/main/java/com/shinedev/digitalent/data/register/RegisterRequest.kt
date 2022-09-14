package com.shinedev.digitalent.data.register


data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)