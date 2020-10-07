package com.westwinglabs.coinbase.websocket

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.westwinglabs.coinbase.CoinbaseException
import com.westwinglabs.coinbase.service.CoinbaseConverterFactory
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

internal class CoinbaseFeed(
    private val feedListener: FeedListener,
    private val mapper: ObjectMapper = CoinbaseConverterFactory.mapper
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        feedListener.onOpen()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        when (val type = decoded.get("type").asText()) {
            "error" -> handleErrorMessage(decoded)
            "subscriptions" -> handleSubscriptionsMessage(text)
            "heartbeat" -> handleHeartbeatMessage(text)
            else -> handleUnknownMessage(type, text)
        }
    }

    private fun handleSubscriptionsMessage(text: String) {
        val decoded = mapper.readValue<SubscriptionResponse>(text)
        feedListener.onSubscriptionsMessage(decoded)
    }

    private fun handleHeartbeatMessage(text: String) {
        val decoded = mapper.readValue<HeartbeatResponse>(text)
        feedListener.onHeartbeatMessage(decoded)
    }

    private fun handleErrorMessage(decoded: JsonNode) {
        val message = decoded.get("message").asText()
        val error = CoinbaseException("Error message received: $message")
        feedListener.onFailure(error)
    }

    private fun handleUnknownMessage(type: String, text: String) {
        val error = CoinbaseException("Unsupported message type received ($type): $text")
        feedListener.onFailure(error)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        val error = CoinbaseException("Unsupported binary message received: $bytes")
        feedListener.onFailure(error)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        feedListener.onClosing(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        feedListener.onClosed(code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        feedListener.onFailure(t)
    }
}
