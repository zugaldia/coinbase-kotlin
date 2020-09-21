package com.westwinglabs.coinbase

import org.junit.Assert
import org.junit.Test

class CoinbaseClientPublicTest : BaseMockedServer() {

    @Test
    fun getProductsMocked() {
        val response = mockedClient.getProducts()
        Assert.assertNotNull(response)
        Assert.assertEquals(6, response?.size)
        Assert.assertEquals("LINK-USDC", response?.first()?.id)
    }

}
