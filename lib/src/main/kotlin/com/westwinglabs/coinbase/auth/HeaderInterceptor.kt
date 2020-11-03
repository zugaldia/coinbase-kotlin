package com.westwinglabs.coinbase.auth

import com.westwinglabs.coinbase.service.CoinbaseService
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.util.*

/**
 * This class adds a number of headers to a request:
 * - CoinbaseService.HEADER_USER_AGENT (all requests)
 * - CoinbaseService.HEADER_TIMESTAMP (when signature is required)
 * - CoinbaseService.HEADER_SIGN (when signature is required)
 */
internal class HeaderInterceptor(
    private val apiSecret: String,
    private val appId: String
) : Interceptor {

    private val signatory = RequestSignatory(apiSecret)
    private val version: String

    init {
        val properties = Properties()
        properties.load(javaClass.getResourceAsStream("/version.properties"))
        version = properties.getProperty("version")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()

        // Always add a user agent
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent
        newRequest.addHeader(CoinbaseService.HEADER_USER_AGENT, "CoinbaseKotlin/$version ($appId)")

        // Only sign requests that include the API key
        val key = request.header(CoinbaseService.HEADER_KEY)
        if (key.isNullOrEmpty()) {
            return chain.proceed(newRequest.build())
        }

        val encodedQuery = request.url().encodedQuery()
        val queryExtra = if (encodedQuery.isNullOrEmpty()) "" else "?$encodedQuery"

        val result = signatory.sign(
            request.method(),
            request.url().encodedPath() + queryExtra,
            requestBodyToString(request.body())
        )

        newRequest
            .addHeader(CoinbaseService.HEADER_TIMESTAMP, result.first)
            .addHeader(CoinbaseService.HEADER_SIGN, result.second)

        return chain.proceed(newRequest.build())
    }

    /*
     * Refs: https://github.com/square/okhttp/issues/1891
     */
    private fun requestBodyToString(body: RequestBody?): String {
        val copy = body ?: return ""
        val buffer = Buffer()
        copy.writeTo(buffer)
        return buffer.readUtf8()
    }
}
