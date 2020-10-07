package com.westwinglabs.coinbase.websocket

import com.fasterxml.jackson.annotation.JsonProperty

abstract class FeedListener {
    abstract fun onOpen()
    open fun onClosing(code: Int, reason: String) {}
    open fun onClosed(code: Int, reason: String) {}
    open fun onFailure(throwable: Throwable) {}
    open fun onSubscriptionsMessage(message: SubscriptionResponse) {}
    open fun onHeartbeatMessage(message: HeartbeatResponse) {}
}

data class SubscribeRequest(
    @JsonProperty("type") val type: String = "subscribe",
    @JsonProperty("channels") val channels: List<String>,
    @JsonProperty("product_ids") val productIds: List<String>
)

data class UnsubscribeRequest(
    @JsonProperty("type") val type: String = "unsubscribe",
    @JsonProperty("channels") val channels: List<String>,
    @JsonProperty("product_ids") val productIds: List<String>
)

data class SubscriptionResponse(
    @JsonProperty("type") val type: String,
    @JsonProperty("channels") val channels: List<SubscriptionChannel>
)

data class SubscriptionChannel(
    @JsonProperty("name") val name: String,
    @JsonProperty("product_ids") val productIds: List<String>
)

data class HeartbeatResponse(
    @JsonProperty("last_trade_id") val lastTradeId: Int,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("sequence") val sequence: Long,
    @JsonProperty("time") val time: String,
    @JsonProperty("type") val type: String
)
