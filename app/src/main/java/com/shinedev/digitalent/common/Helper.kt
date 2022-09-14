package com.shinedev.digitalent.common

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.withDateFormat(): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = format.parse(this) as Date

        val textDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
        val textTime = SimpleDateFormat("HH:mm:ss.SS", Locale.US).format(date)
        "$textDate $textTime"
    } catch (e: Exception) {
        this
    }
}