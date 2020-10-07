package com.westwinglabs.coinbase

import org.junit.Assert
import org.junit.Test

class CoinbaseClientTest {

    private val client = CoinbaseClient()
    private val sampleString = "2014-11-06T10:34:47.123456Z"

    @Test
    fun testParseDateTime() {
        val parsed = client.parseDateTime(sampleString)

        Assert.assertEquals(2014, parsed.year)
        Assert.assertEquals(11, parsed.monthOfYear)
        Assert.assertEquals(6, parsed.dayOfMonth)
        Assert.assertEquals(10, parsed.hourOfDay)
        Assert.assertEquals(34, parsed.minuteOfHour)
        Assert.assertEquals(47, parsed.secondOfMinute)
        Assert.assertEquals(123, parsed.millisOfSecond)
        Assert.assertEquals("UTC", parsed.zone.id)
    }

    @Test
    fun testFormatDateTime() {
        val date = client.parseDateTime(sampleString)

        // Note: microsecond precision is not supported by Joda Time
        Assert.assertEquals("2014-11-06T10:34:47.123Z", client.formatDateTime(date))
    }
}
