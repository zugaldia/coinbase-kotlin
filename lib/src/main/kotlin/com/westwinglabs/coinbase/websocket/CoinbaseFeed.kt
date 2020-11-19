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
            "status" -> handleStatusMessage(text)
            "ticker" -> handleTickerMessage(text)
            "snapshot" -> handleSnapshotMessage(text)
            "l2update" -> handleLevel2UpdateMessage(text)
            "received" -> handleReceivedMessage(text)
            "open" -> handleOpenMessage(text)
            "done" -> handleDoneMessage(text)
            "match" -> handleMatchMessage(text)
            "change" -> handleChangeMessage(text)
            "activate" -> handleActivateMessage(text)
            else -> handleUnknownMessage(type, text)
        }
    }

    private fun handleErrorMessage(decoded: JsonNode) {
        val message = decoded.get("message").asText()
        val error = CoinbaseException("Error message received: $message")
        feedListener.onFailure(error)
    }

    private fun handleSubscriptionsMessage(text: String) {
        val decoded = mapper.readValue<SubscriptionsMessage>(text)
        feedListener.onSubscriptionsMessage(decoded)
    }

    private fun handleHeartbeatMessage(text: String) {
        val decoded = mapper.readValue<HeartbeatMessage>(text)
        feedListener.onHeartbeatMessage(decoded)
    }

    private fun handleStatusMessage(text: String) {
        val decoded = mapper.readValue<StatusMessage>(text)
        feedListener.onStatusMessage(decoded)
    }

    private fun handleTickerMessage(text: String) {
        val decoded = mapper.readValue<TickerMessage>(text)
        feedListener.onTickerMessage(decoded)
    }

    private fun handleSnapshotMessage(text: String) {
        val decoded = mapper.readValue<SnapshotMessage>(text)
        feedListener.onSnapshotMessage(decoded)
    }

    private fun handleLevel2UpdateMessage(text: String) {
        val decoded = mapper.readValue<Level2UpdateMessage>(text)
        feedListener.onLevel2UpdateMessage(decoded)
    }

    private fun handleReceivedMessage(text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        feedListener.onReceivedMessage(decoded)
    }

    private fun handleOpenMessage(text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        feedListener.onOpenMessage(decoded)
    }

    private fun handleDoneMessage(text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        feedListener.onDoneMessage(decoded)
    }

    private fun handleMatchMessage(text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        feedListener.onMatchMessage(decoded)
    }

    private fun handleChangeMessage(text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        feedListener.onChangeMessage(decoded)
    }

    private fun handleActivateMessage(text: String) {
        val decoded = mapper.readValue<JsonNode>(text)
        feedListener.onActivateMessage(decoded)
    }

    private fun handleUnknownMessage(type: String, text: String) {
        // Fail silently. New message types can be added at any point in time
        // and clients are expected to ignore messages they do not support.
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
