package com.westwinglabs.coinbase.cli

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.westwinglabs.coinbase.CoinbaseCallback
import com.westwinglabs.coinbase.CoinbaseClient
import com.westwinglabs.coinbase.auth.RequestSignatory
import com.westwinglabs.coinbase.service.AccountsResponse
import com.westwinglabs.coinbase.websocket.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.cli.CommandLine
import org.apache.logging.log4j.LogManager
import java.io.File

class CoinbaseCli : FeedListener() {

    private val logger = LogManager.getLogger()

    private val mapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())

    private lateinit var websocketClient: CoinbaseClient
    private var totalMessages = 0

    fun sampleSignature(parsed: CommandLine) {
        val signatory = RequestSignatory(parsed.getOptionValue(OPTION_API_SECRET))
        val result = signatory.sign(
            method = parsed.getOptionValue(OPTION_SIGNATURE_METHOD, "GET"),
            path = parsed.getOptionValue(OPTION_SIGNATURE_PATH),
            body = parsed.getOptionValue(OPTION_SIGNATURE_BODY, "")
        )

        val response = hashMapOf("timestamp" to result.first, "signature" to result.second)
        printEncoded(response)
    }

    fun samplePrivate(parsed: CommandLine) {
        // This function also shows how to customize the HTTP client.
        // In this example we add basic logging.
        val logging = HttpLoggingInterceptor { message -> logger.info(message) }
            .apply { level = HttpLoggingInterceptor.Level.BASIC }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val privateClient = CoinbaseClient(
            apiEndpoint = getEndpoints(parsed).first,
            apiKey = parsed.getOptionValue(OPTION_API_KEY),
            apiSecret = parsed.getOptionValue(OPTION_API_SECRET),
            apiPassphrase = parsed.getOptionValue(OPTION_API_PASSPHRASE),
            okHttpClient = okHttpClient
        )

        // Shows how to obtain an async response
        privateClient.getAccounts(object : CoinbaseCallback<AccountsResponse>() {
            override fun onResponse(result: AccountsResponse?) {
                printEncoded(result)
            }
        })
    }

    fun samplePublic(parsed: CommandLine) {
        // Custom HTTP client with caching
        val okHttpClient = OkHttpClient.Builder()
            .cache(Cache(File("/tmp"), 10 * 1_024 * 1_024)) // 10 MB in bytes
            .build()

        val client = CoinbaseClient(apiEndpoint = getEndpoints(parsed).first, okHttpClient = okHttpClient)
        val response = client.getProducts()
        client.close()

        printEncoded(response)
    }

    fun sampleWebsocket(parsed: CommandLine) {
        // We'll get 10 heartbeat messages and exit
        websocketClient = CoinbaseClient(feedEndpoint = getEndpoints(parsed).second)
        websocketClient.openFeed(this)
    }

    override fun onOpen() {
        val request = SubscribeRequest(
            channels = listOf(CoinbaseClient.CHANNEL_HEARTBEAT, CoinbaseClient.CHANNEL_LEVEL2),
            productIds = listOf("ETH-BTC", "BTC-USD")
        )

        println("Feed connection open, sending subscription request.")
        websocketClient.subscribe(request)
    }

    override fun onClosing(code: Int, reason: String) {
        val message = if (reason.isBlank()) "No reason was given." else reason
        println("On closing ($code): $message")
    }

    override fun onClosed(code: Int, reason: String) {
        val message = if (reason.isBlank()) "No reason was given." else reason
        println("On closed ($code): $message")
    }

    override fun onFailure(throwable: Throwable) {
        println("On failure: ${throwable.message}")
        throwable.printStackTrace()
    }

    override fun onSubscriptionsMessage(message: SubscriptionsMessage) = printEncoded(message)
    override fun onStatusMessage(message: StatusMessage) = printEncoded(message)
    override fun onTickerMessage(message: TickerMessage) = printEncoded(message)
    override fun onSnapshotMessage(message: SnapshotMessage) = printEncoded(message)
    override fun onLevel2UpdateMessage(message: Level2UpdateMessage) = printEncoded(message)
    override fun onReceivedMessage(message: JsonNode) = printEncoded(message)
    override fun onOpenMessage(message: JsonNode) = printEncoded(message)
    override fun onDoneMessage(message: JsonNode) = printEncoded(message)
    override fun onMatchMessage(message: JsonNode) = printEncoded(message)
    override fun onChangeMessage(message: JsonNode) = printEncoded(message)
    override fun onActivateMessage(message: JsonNode) = printEncoded(message)

    override fun onHeartbeatMessage(message: HeartbeatMessage) {
        printEncoded(message)

        totalMessages += 1
        if (totalMessages >= 10) {
            println("Disconnecting after 10 heartbeats.")
            websocketClient.closeFeed()
            websocketClient.close()
        }
    }

    private fun printEncoded(thing: Any?) = println(mapper.writeValueAsString(thing))

    private fun getEndpoints(parsed: CommandLine): Pair<String, String> =
        when (parsed.getOptionValue(OPTION_API_ENDPOINT)) {
            // Default to sandbox for this demo (CoinbaseClient defaults to production otherwise)
            "production" -> Pair(CoinbaseClient.ENDPOINT_PRODUCTION, CoinbaseClient.WEBSOCKET_PRODUCTION)
            else -> Pair(CoinbaseClient.ENDPOINT_SANDBOX, CoinbaseClient.WEBSOCKET_SANDBOX)
        }
}
