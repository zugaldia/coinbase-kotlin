package com.westwinglabs.coinbase

import com.westwinglabs.coinbase.service.CoinbaseService.Companion.ENDPOINT_PRIVATE_ACCOUNTS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.ENDPOINT_PUBLIC_PRODUCTS
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before

open class BaseMockedServer {

    private lateinit var server: MockWebServer
    internal lateinit var mockedClient: CoinbaseClient

    private fun loadFixture(path: String): String =
        IOUtils.resourceToString("/fixtures/$path", Charsets.UTF_8)

    @Before
    fun setup() {
        server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val response: String = when {
                    request.path.contains(ENDPOINT_PRIVATE_ACCOUNTS) -> loadFixture("")
                    request.path.contains(ENDPOINT_PUBLIC_PRODUCTS) -> loadFixture("public/get_products.json")
                    else -> "{}"
                }

                return MockResponse().setBody(response)
            }
        }

        server.start()
        mockedClient = CoinbaseClient(apiEndpoint = server.url("").toString())
    }

    @After
    fun teardown() {
        server.shutdown()
    }
}