package com.shinedev.digitalent.common

import org.junit.Assert
import org.junit.Test
import java.time.format.DateTimeParseException
import java.time.zone.ZoneRulesException

class DateFormatterTest {

    @Test
    fun `given correct ISO 8601 format then should format correctly`() {
        val currentDate = "2022-02-02T10:10:10Z"
        Assert.assertEquals(
            "February 2, 2022 at 5:10:10 PM WIB",
            DateFormatter.formatDate(currentDate, "Asia/Jakarta")
        )
        Assert.assertEquals(
            "February 2, 2022 at 6:10:10 PM WITA",
            DateFormatter.formatDate(currentDate, "Asia/Makassar")
        )
        Assert.assertEquals(
            "February 2, 2022 at 7:10:10 PM WIT",
            DateFormatter.formatDate(currentDate, "Asia/Jayapura")
        )
    }

    @Test
    fun `given wrong ISO 8601 format then should throw error`() {
        val wrongFormat = "02-12-2022"
        Assert.assertThrows(DateTimeParseException::class.java) {
            DateFormatter.formatDate(wrongFormat, "Asia/Jakarta")
        }
    }

    @Test
    fun `given invalid timezone then should throw error`() {
        val wrongFormat = "2022-02-02T10:10:10Z"
        Assert.assertThrows(ZoneRulesException::class.java) {
            DateFormatter.formatDate(wrongFormat, "Asia/Ambon")
        }
    }
}