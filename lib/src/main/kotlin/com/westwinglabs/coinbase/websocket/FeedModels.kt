package com.westwinglabs.coinbase.websocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

abstract class FeedListener {
    abstract fun onOpen()
    open fun onClosing(code: Int, reason: String) {}
    open fun onClosed(code: Int, reason: String) {}
    open fun onFailure(throwable: Throwable) {}

    open fun onSubscriptionsMessage(message: SubscriptionsMessage) {}
    open fun onHeartbeatMessage(message: HeartbeatMessage) {}
    open fun onStatusMessage(message: StatusMessage) {}
    open fun onTickerMessage(message: TickerMessage) {}
    open fun onSnapshotMessage(message: SnapshotMessage) {}
    open fun onLevel2UpdateMessage(message: Level2UpdateMessage) {}
    open fun onReceivedMessage(message: JsonNode) {}
    open fun onOpenMessage(message: JsonNode) {}
    open fun onDoneMessage(message: JsonNode) {}
    open fun onMatchMessage(message: JsonNode) {}
    open fun onChangeMessage(message: JsonNode) {}
    open fun onActivateMessage(message: JsonNode) {}
}

/*
 * Subscriptions
 */

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

data class SubscriptionsMessage(
    @JsonProperty("type") val type: String,
    @JsonProperty("channels") val channels: List<SubscriptionsChannel>
)

data class SubscriptionsChannel(
    @JsonProperty("name") val name: String,
    @JsonProperty("product_ids") val productIds: List<String>
)

/*
 * The heartbeat channel
 */

data class HeartbeatMessage(
    @JsonProperty("last_trade_id") val lastTradeId: Int,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("sequence") val sequence: Long,
    @JsonProperty("time") val time: String,
    @JsonProperty("type") val type: String
)

/*
 * The status channel
 */

data class StatusMessage(
    @JsonProperty("type") val type: String,
    @JsonProperty("products") val products: List<ProductStatus>,
    @JsonProperty("currencies") val currencies: List<CurrencyStatus>
)

data class ProductStatus(
    @JsonProperty("base_currency") val baseCurrency: String,
    @JsonProperty("base_increment") val baseIncrement: String,
    @JsonProperty("base_max_size") val baseMaxSize: String,
    @JsonProperty("base_min_size") val baseMinSize: String,
    @JsonProperty("cancel_only") val cancelOnly: Boolean,
    @JsonProperty("display_name") val displayName: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("limit_only") val limitOnly: Boolean,
    @JsonProperty("margin_enabled") val marginEnabled: Boolean,
    @JsonProperty("max_market_funds") val maxMarketFunds: String,
    @JsonProperty("min_market_funds") val minMarketFunds: String,
    @JsonProperty("post_only") val postOnly: Boolean,
    @JsonProperty("quote_currency") val quoteCurrency: String,
    @JsonProperty("quote_increment") val quoteIncrement: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("status_message") val statusMessage: String,
    @JsonProperty("type") val type: String
)

data class CurrencyStatus(
    @JsonProperty("convertible_to") val convertibleTo: List<String>,
    @JsonProperty("details") val details: CurrencyDetails,
    @JsonProperty("funding_account_id") val fundingAccountId: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("max_precision") val maxPrecision: String,
    @JsonProperty("min_size") val minSize: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("status_message") val statusMessage: String
)

data class CurrencyDetails(
    @JsonProperty("crypto_address_link") val cryptoAddressLink: String,
    @JsonProperty("crypto_transaction_link") val cryptoTransactionLink: String,
    @JsonProperty("display_name") val displayName: String?,
    @JsonProperty("group_types") val groupTypes: List<String>?,
    @JsonProperty("network_confirmations") val networkConfirmations: Int,
    @JsonProperty("push_payment_methods") val pushPaymentMethods: List<String>,
    @JsonProperty("sort_order") val sortOrder: Int,
    @JsonProperty("symbol") val symbol: String,
    @JsonProperty("type") val type: String
)

/*
 * The ticker channel
 */

data class TickerMessage(
    @JsonProperty("best_ask") val bestAsk: String,
    @JsonProperty("best_bid") val bestBid: String,
    @JsonProperty("high_24h") val high24h: String,
    @JsonProperty("last_size") val lastSize: String,
    @JsonProperty("low_24h") val low24h: String,
    @JsonProperty("open_24h") val open24h: String,
    @JsonProperty("price") val price: String,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("sequence") val sequence: Long,
    @JsonProperty("side") val side: String,
    @JsonProperty("time") val time: String,
    @JsonProperty("trade_id") val tradeId: Int,
    @JsonProperty("type") val type: String,
    @JsonProperty("volume_24h") val volume24h: String,
    @JsonProperty("volume_30d") val volume30d: String
)

/*
 * The level2 channel
 */

data class SnapshotMessage(
    @JsonProperty("asks") val asks: List<List<String>>,
    @JsonProperty("bids") val bids: List<List<String>>,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("type") val type: String
)

data class Level2UpdateMessage(
    @JsonProperty("changes") val changes: List<List<String>>,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("time") val time: String,
    @JsonProperty("type") val type: String
)
