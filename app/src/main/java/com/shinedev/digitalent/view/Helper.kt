package com.shinedev.digitalent.view

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.withDateFormat(): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = format.parse(this) as Date
        DateFormat.getDateInstance(DateFormat.FULL).format(date)
    } catch (e: Exception) {
        this
    }
}

/*
fun getErrorBody(response: ): BaseResponse {
    return if (response.code() != 502) {
        val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
        Gson().fromJson(jsonObject.toString(), BaseResponse::class.java)
    } else {
        return BaseResponse(true, "Bad Gateway")
    }
}*/
