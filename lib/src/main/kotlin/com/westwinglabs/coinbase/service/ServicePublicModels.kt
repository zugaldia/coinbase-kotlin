package com.westwinglabs.coinbase.service

import com.fasterxml.jackson.annotation.JsonProperty

class ProductsResponse : ArrayList<ProductsResponseItem>()

data class ProductsResponseItem(
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
    @JsonProperty("trading_disabled") val tradingDisabled: Boolean
)

data class ProductResponse(
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
    @JsonProperty("trading_disabled") val tradingDisabled: Boolean
)

data class ProductBookResponse(
    @JsonProperty("asks") val asks: List<List<Any>>,
    @JsonProperty("bids") val bids: List<List<Any>>,
    @JsonProperty("sequence") val sequence: Long
)

data class ProductTickerResponse(
    @JsonProperty("ask") val ask: String,
    @JsonProperty("bid") val bid: String,
    @JsonProperty("price") val price: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("time") val time: String,
    @JsonProperty("trade_id") val tradeId: Int,
    @JsonProperty("volume") val volume: String
)

class ProductTradesResponse : ArrayList<ProductTradesResponseItem>()

data class ProductTradesResponseItem(
    @JsonProperty("price") val price: String,
    @JsonProperty("side") val side: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("time") val time: String,
    @JsonProperty("trade_id") val tradeId: Int
)

data class ProductStatsResponse(
    @JsonProperty("high") val high: String,
    @JsonProperty("last") val last: String,
    @JsonProperty("low") val low: String,
    @JsonProperty("open") val `open`: String,
    @JsonProperty("volume") val volume: String,
    @JsonProperty("volume_30day") val volume30day: String
)

class CurrenciesResponse : ArrayList<CurrenciesResponseItem>()

data class CurrenciesResponseItem(
    @JsonProperty("convertible_to") val convertibleTo: List<String>?, // optional
    @JsonProperty("details") val details: CurrencyDetails,
    @JsonProperty("id") val id: String,
    @JsonProperty("max_precision") val maxPrecision: String,
    @JsonProperty("message") val message: String?, // optional
    @JsonProperty("min_size") val minSize: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("status") val status: String
)

data class CurrencyDetails(
    @JsonProperty("crypto_address_link") val cryptoAddressLink: String?, // optional
    @JsonProperty("crypto_transaction_link") val cryptoTransactionLink: String?, // optional
    @JsonProperty("display_name") val displayName: String?, // optional
    @JsonProperty("group_types") val groupTypes: List<String>?, // optional
    @JsonProperty("min_withdrawal_amount") val minWithdrawalAmount: Double,
    @JsonProperty("network_confirmations") val networkConfirmations: Int,
    @JsonProperty("processing_time_seconds") val processingTimeSeconds: Int,
    @JsonProperty("push_payment_methods") val pushPaymentMethods: List<String>,
    @JsonProperty("sort_order") val sortOrder: Int,
    @JsonProperty("symbol") val symbol: String?, // optional
    @JsonProperty("type") val type: String
)

data class TimeResponse(
    @JsonProperty("epoch") val epoch: Double,
    @JsonProperty("iso") val iso: String
)
