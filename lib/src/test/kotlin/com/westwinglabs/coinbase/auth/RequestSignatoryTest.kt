package com.westwinglabs.coinbase.auth

import org.junit.Assert
import org.junit.Test

/*
 * Confirm that we match the results provided by the reference docs:
 * https://docs.pro.coinbase.com/#signing-a-message
 *
 * Values come from `generate_signature.js`.
 */
class RequestSignatoryTest {

    // Sandbox endpoint
    private val apiSecret = System.getenv("COINBASE_API_SECRET")
    private val signatory = RequestSignatory(apiSecret)

    @Test
    fun testEmptyBody() {
        val timestamp = "1604352923.169"
        val result = signatory.sign(timestamp, "GET", "/accounts", "")
        Assert.assertEquals(timestamp, result.first)
        Assert.assertEquals("j5ge2C+8wjwVm6xdywVSUUj6qMv/G/xPMthU2Qc3mtw=", result.second)
    }

    @Test
    fun testNonEmptyBody() {
        val timestamp = "1604353024.941"
        val body = "{\"size\":\"0.01\",\"price\":\"0.100\",\"side\":\"buy\",\"product_id\":\"BTC-USD\"}"
        val result = signatory.sign(timestamp, "POST", "/orders", body)
        Assert.assertEquals(timestamp, result.first)
        Assert.assertEquals("3jc2jgkGpbDU+pYXqxuVAq0SI/ueTh0etSE6KSL7w0c=", result.second)
    }
}
