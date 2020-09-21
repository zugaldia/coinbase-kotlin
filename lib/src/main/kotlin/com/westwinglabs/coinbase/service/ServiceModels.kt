package com.westwinglabs.coinbase.service

import com.fasterxml.jackson.annotation.JsonProperty

const val DEFAULT_RESPONSE_CODE = 200
const val DEFAULT_RESPONSE_MESSAGE = "OK"

interface BaseModel {
    val code: Int
    val message: String
}

open class BaseListModel<T> : ArrayList<T>(), BaseModel {
    override val code: Int = DEFAULT_RESPONSE_CODE
    override val message: String = DEFAULT_RESPONSE_MESSAGE
}

/*
 * Private API
 */

class AccountsResponse : BaseListModel<AccountsResponseItem>()

data class AccountsResponseItem(
    @JsonProperty("available")
    val available: String,
    @JsonProperty("balance")
    val balance: String,
    @JsonProperty("currency")
    val currency: String,
    @JsonProperty("hold")
    val hold: String,
    @JsonProperty("id")
    val id: String,
    @JsonProperty("profile_id")
    val profileId: String,
    @JsonProperty("trading_enabled")
    val tradingEnabled: Boolean
)

/*
 * Public API
 */

class ProductsResponse : BaseListModel<ProductsResponseItem>()

data class ProductsResponseItem(
    @JsonProperty("base_currency")
    val baseCurrency: String,
    @JsonProperty("base_increment")
    val baseIncrement: String,
    @JsonProperty("base_max_size")
    val baseMaxSize: String,
    @JsonProperty("base_min_size")
    val baseMinSize: String,
    @JsonProperty("cancel_only")
    val cancelOnly: Boolean,
    @JsonProperty("display_name")
    val displayName: String,
    @JsonProperty("id")
    val id: String,
    @JsonProperty("limit_only")
    val limitOnly: Boolean,
    @JsonProperty("margin_enabled")
    val marginEnabled: Boolean,
    @JsonProperty("max_market_funds")
    val maxMarketFunds: String,
    @JsonProperty("min_market_funds")
    val minMarketFunds: String,
    @JsonProperty("post_only")
    val postOnly: Boolean,
    @JsonProperty("quote_currency")
    val quoteCurrency: String,
    @JsonProperty("quote_increment")
    val quoteIncrement: String,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("status_message")
    val statusMessage: String,
    @JsonProperty("trading_disabled")
    val tradingDisabled: Boolean
)
