package com.westwinglabs.coinbase

import com.westwinglabs.coinbase.auth.HeaderInterceptor
import com.westwinglabs.coinbase.service.AccountsResponse
import com.westwinglabs.coinbase.service.CoinbaseConverterFactory
import com.westwinglabs.coinbase.service.CoinbaseService
import com.westwinglabs.coinbase.service.ProductsResponse
import com.westwinglabs.coinbase.websocket.CoinbaseFeed
import com.westwinglabs.coinbase.websocket.FeedListener
import com.westwinglabs.coinbase.websocket.SubscribeRequest
import com.westwinglabs.coinbase.websocket.UnsubscribeRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.apache.commons.lang3.Validate
import retrofit2.Retrofit

class CoinbaseClient(
    private val apiKey: String = "",
    private val apiSecret: String = "",
    private val apiPassphrase: String = "",
    private val appId: String = "com.westwinglabs.coinbase",
    private val apiEndpoint: String = ENDPOINT_PRODUCTION,
    private val feedEndpoint: String = WEBSOCKET_PRODUCTION,
    private val okHttpClient: OkHttpClient = OkHttpClient()
) {

    companion object {
        const val ENDPOINT_SANDBOX = "https://api-public.sandbox.pro.coinbase.com"
        const val ENDPOINT_PRODUCTION = "https://api.pro.coinbase.com"

        const val WEBSOCKET_SANDBOX = "wss://ws-feed-public.sandbox.pro.coinbase.com"
        const val WEBSOCKET_PRODUCTION = "wss://ws-feed.pro.coinbase.com"
    }

    private val service: CoinbaseService

    private lateinit var feed: CoinbaseFeed
    private lateinit var webSocket: WebSocket

    init {
        Validate.notBlank(appId, "App ID cannot be empty.")
        Validate.notBlank(apiEndpoint, "API endpoint cannot be empty.")
        Validate.notBlank(feedEndpoint, "Websocket endpoint cannot be empty.")

        val clientWithSigning = okHttpClient.newBuilder()
            .addInterceptor(HeaderInterceptor(apiSecret, appId))
            .build()

        val retrofit = Retrofit.Builder()
            .client(clientWithSigning)
            .baseUrl(apiEndpoint)
            .addConverterFactory(CoinbaseConverterFactory.create())
            .build()

        service = retrofit.create(CoinbaseService::class.java)
    }

    /*
     * Private API
     */

    fun getAccounts(): AccountsResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .getAccounts(apiKey, apiPassphrase).execute().body()

    fun getAccounts(callback: CoinbaseCallback<AccountsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase)
            .getAccounts(apiKey, apiPassphrase).enqueueWith(callback)

    /*
     * Public API
     */

    fun getProducts(): ProductsResponse? = service.getProducts().execute().body()

    fun getProducts(callback: CoinbaseCallback<ProductsResponse>) =
        service.getProducts().enqueueWith(callback)

    /*
     * Websocket logic
     */

    fun openFeed(feedListener: FeedListener) {
        feed = CoinbaseFeed(feedListener)

        val request = Request.Builder().url(feedEndpoint).build()
        webSocket = okHttpClient.newWebSocket(request, feed)
    }

    fun closeFeed(code: Int = 1000, reason: String = "") {
        webSocket.close(code, reason)
    }

    fun subscribe(request: SubscribeRequest) {
        val encoded = CoinbaseConverterFactory.mapper.writeValueAsString(request)
        webSocket.send(encoded)
    }

    fun unsubscribe(request: UnsubscribeRequest) {
        val encoded = CoinbaseConverterFactory.mapper.writeValueAsString(request)
        webSocket.send(encoded)
    }
}
