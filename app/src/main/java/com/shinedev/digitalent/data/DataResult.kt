package com.shinedev.digitalent.data

sealed class DataResult<out R> private constructor() {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val message: String) : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()
}