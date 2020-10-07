package com.westwinglabs.coinbase

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import com.westwinglabs.coinbase.service.CoinbaseConverterFactory
import com.westwinglabs.coinbase.service.ProductResponse
import com.westwinglabs.coinbase.service.ProductsResponse
import org.junit.Assert
import org.junit.Test

class CoinbaseClientPublicTest : BaseMockedServer() {

    /*
     * Expected error behavior
     */

    @Test(expected = MismatchedInputException::class)
    fun testListErrorResponse() {
        val errorResponse = loadFixture("public/get_error.json")
        CoinbaseConverterFactory.mapper.readValue<ProductsResponse>(errorResponse)
    }

    @Test(expected = MissingKotlinParameterException::class)
    fun testErrorResponse() {
        val errorResponse = loadFixture("public/get_error.json")
        CoinbaseConverterFactory.mapper.readValue<ProductResponse>(errorResponse)
    }

    @Test
    fun testErrorClient() {
        val response = mockedClient.getProduct(PRODUCT_INVALID)
        Assert.assertNull(response)
    }

    /*
     * Test endpoints
     */

    @Test
    fun testGetProductsMocked() {
        val response = mockedClient.getProducts()
        Assert.assertNotNull(response)
        Assert.assertEquals(97, response?.size)
        Assert.assertEquals(1, response?.filter { it.id == "BTC-USD" }?.count())
    }

    @Test
    fun testGetProductMocked() {
        val response = mockedClient.getProduct(PRODUCT_VALID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProductBookMocked() {
        val response = mockedClient.getProductBook(PRODUCT_VALID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProductTickerMocked() {
        val response = mockedClient.getProductTicker(PRODUCT_VALID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProductTradesMocked() {
        val response = mockedClient.getProductTrades(PRODUCT_VALID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProductCandlesMocked() {
        val response = mockedClient.getProductCandles(PRODUCT_VALID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProductStatsMocked() {
        val response = mockedClient.getProductStats(PRODUCT_VALID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetCurrenciesMocked() {
        val response = mockedClient.getCurrencies()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetTimeMocked() {
        val response = mockedClient.getTime()
        Assert.assertNotNull(response)
    }
}
