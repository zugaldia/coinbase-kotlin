package com.westwinglabs.coinbase.service

import retrofit2.Call
import retrofit2.http.*

internal interface CoinbaseService {

    companion object {
        // All requests will include this header
        const val HEADER_USER_AGENT = "User-Agent"

        // If values for these headers are set, request will be signed.
        const val HEADER_KEY = "CB-ACCESS-KEY"
        const val HEADER_PASSPHRASE = "CB-ACCESS-PASSPHRASE"

        // The values for these headers are added in the interceptor.
        const val HEADER_TIMESTAMP = "CB-ACCESS-TIMESTAMP"
        const val HEADER_SIGN = "CB-ACCESS-SIGN"

        // Private API
        const val PATH_PRIVATE_GET_ACCOUNTS = "/accounts"
        const val PATH_PRIVATE_GET_ACCOUNT = "/accounts/{accountId}"
        const val PATH_PRIVATE_GET_ACCOUNT_LEDGER = "/accounts/{accountId}/ledger"
        const val PATH_PRIVATE_GET_ACCOUNT_HOLDS = "/accounts/{accountId}/holds"
        const val PATH_PRIVATE_POST_ORDERS = "/orders"
        const val PATH_PRIVATE_DELETE_ORDER = "/orders/{id}"
        const val PATH_PRIVATE_DELETE_ORDER_CLIENT = "/orders/client:{clientOid}"
        const val PATH_PRIVATE_DELETE_ORDERS = "/orders"
        const val PATH_PRIVATE_GET_ORDERS = "/orders"
        const val PATH_PRIVATE_GET_ORDER = "/orders/{id}"
        const val PATH_PRIVATE_GET_ORDER_CLIENT = "/orders/client:{clientOid}"
        const val PATH_PRIVATE_GET_FILLS = "/fills"
        const val PATH_PRIVATE_GET_USER_EXCHANGE_LIMITS = "/users/self/exchange-limits"
        const val PATH_PRIVATE_GET_TRANSFERS = "/transfers"
        const val PATH_PRIVATE_GET_TRANSFER = "/transfers/{transferId}"
        const val PATH_PRIVATE_POST_DEPOSIT_PAYMENT_METHOD = "/deposits/payment-method"
        const val PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT = "/deposits/coinbase-account"
        const val PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT_ADDRESS =
            "/coinbase-accounts/{coinbaseAccountId}/addresses"
        const val PATH_PRIVATE_POST_WITHDRAWAL_PAYMENT_METHOD = "/withdrawals/payment-method"
        const val PATH_PRIVATE_POST_WITHDRAWAL_COINBASE_ACCOUNT = "/withdrawals/coinbase-account"
        const val PATH_PRIVATE_POST_WITHDRAWAL_CRYPTO = "/withdrawals/crypto"
        const val PATH_PRIVATE_GET_WITHDRAWAL_FEE_ESTIMATE = "/withdrawals/fee-estimate"
        const val PATH_PRIVATE_POST_CONVERSIONS = "/conversions"
        const val PATH_PRIVATE_GET_PAYMENT_METHODS = "/payment-methods"
        const val PATH_PRIVATE_GET_COINBASE_ACCOUNTS = "/coinbase-accounts"
        const val PATH_PRIVATE_GET_FEES = "/fees"
        const val PATH_PRIVATE_POST_REPORTS = "/reports"
        const val PATH_PRIVATE_GET_REPORT = "/reports/{reportId}"
        const val PATH_PRIVATE_GET_PROFILES = "/profiles"
        const val PATH_PRIVATE_GET_PROFILE = "/profiles/{profileId}"
        const val PATH_PRIVATE_POST_PROFILES_TRANSFER_FUNDS = "/profiles/transfer"
        const val PATH_PRIVATE_GET_USER_TRAILING_VOLUME = "/users/self/trailing-volume"
        const val PATH_PRIVATE_GET_MARGIN_PROFILE_INFORMATION = "/margin/profile_information"
        const val PATH_PRIVATE_GET_MARGIN_BUYING_POWER = "/margin/buying_power"
        const val PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER = "/margin/withdrawal_power"
        const val PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER_ALL = "/margin/withdrawal_power_all"
        const val PATH_PRIVATE_GET_MARGIN_EXIT_PLAN = "/margin/exit_plan"
        const val PATH_PRIVATE_GET_MARGIN_LIQUIDATION_HISTORY = "/margin/liquidation_history"
        const val PATH_PRIVATE_GET_MARGIN_POSITION_REFRESH_AMOUNTS = "/margin/position_refresh_amounts"
        const val PATH_PRIVATE_GET_MARGIN_STATUS = "/margin/status"
        const val PATH_PRIVATE_GET_ORACLE = "/oracle"

        // Public API
        const val PATH_PUBLIC_GET_PRODUCTS = "/products"
        const val PATH_PUBLIC_GET_PRODUCT = "/products/{productId}"
        const val PATH_PUBLIC_GET_PRODUCT_BOOK = "/products/{productId}/book"
        const val PATH_PUBLIC_GET_PRODUCT_TICKER = "/products/{productId}/ticker"
        const val PATH_PUBLIC_GET_PRODUCT_TRADES = "/products/{productId}/trades"
        const val PATH_PUBLIC_GET_PRODUCT_CANDLES = "/products/{productId}/candles"
        const val PATH_PUBLIC_GET_PRODUCT_STATS = "/products/{productId}/stats"
        const val PATH_PUBLIC_GET_CURRENCIES = "/currencies"
        const val PATH_PUBLIC_GET_TIME = "/time"
    }

    /*
     * Private API
     *
     * Private endpoints are available for order management, and account management. Every private request is signed.
     */

    @GET(PATH_PRIVATE_GET_ACCOUNTS)
    fun getAccounts(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<AccountsResponse>

    @GET(PATH_PRIVATE_GET_ACCOUNT)
    fun getAccount(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("accountId") accountId: String
    ): Call<AccountResponseItem>

    @GET(PATH_PRIVATE_GET_ACCOUNT_LEDGER)
    fun getAccountLedger(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("accountId") accountId: String
    ): Call<AccountLedgerResponse>

    @GET(PATH_PRIVATE_GET_ACCOUNT_HOLDS)
    fun getAccountHolds(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("accountId") accountId: String
    ): Call<AccountHoldsResponse>

    @POST(PATH_PRIVATE_POST_ORDERS)
    fun postOrder(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("side") side: String,
        @Query("product_id") productId: String,
        @Query("price") price: String,
        @Query("size") size: String,
        @Query("client_oid") clientOid: String?,
        @Query("type") type: String?,
        @Query("stp") stp: String?,
        @Query("stop") stop: String?,
        @Query("stop_price") stopPrice: String?,
        @Query("time_in_force") timeInForce: String?,
        @Query("cancel_after") cancelAfter: String?,
        @Query("post_only") postOnly: String?,
        @Query("funds") funds: String?
    ): Call<PostOrderResponse>

    @DELETE(PATH_PRIVATE_DELETE_ORDER)
    fun cancelOrder(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("id") orderId: String,
        @Query("product_id") productId: String?
    ): Call<String>

    @DELETE(PATH_PRIVATE_DELETE_ORDER_CLIENT)
    fun cancelOrdersByClientId(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("clientOid") orderId: String,
        @Query("product_id") productId: String?
    ): Call<String>

    @DELETE(PATH_PRIVATE_DELETE_ORDERS)
    fun cancelOrders(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("product_id") productId: String?
    ): Call<List<String>>

    @GET(PATH_PRIVATE_GET_ORDERS)
    fun getOrders(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("status") status: String?,
        @Query("product_id") productId: String?
    ): Call<OrdersResponse>

    @GET(PATH_PRIVATE_GET_ORDER)
    fun getOrder(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("id") orderId: String
    ): Call<OrderResponse>

    @GET(PATH_PRIVATE_GET_ORDER_CLIENT)
    fun getOrderByClientId(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("clientOid") orderId: String
    ): Call<OrderResponse>

    @GET(PATH_PRIVATE_GET_FILLS)
    fun getFills(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("order_id") orderId: String?,
        @Query("product_id") productId: String?
    ): Call<FillsResponse>

    @GET(PATH_PRIVATE_GET_USER_EXCHANGE_LIMITS)
    fun getExchangeLimits(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<ExchangeLimitsResponse>

    @GET(PATH_PRIVATE_GET_TRANSFERS)
    fun getTransfers(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("type") type: String,
        @Query("product_id") productId: String?,
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("limit") limit: String?
    ): Call<TransfersResponse>

    @GET(PATH_PRIVATE_GET_TRANSFER)
    fun getTransfer(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("transferId") transferId: String
    ): Call<TransfersResponseItem>

    @POST(PATH_PRIVATE_POST_DEPOSIT_PAYMENT_METHOD)
    fun depositFromPaymentMethod(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("payment_method_id") paymentMethodId: String
    ): Call<DepositFromPaymentMethodResponse>

    @POST(PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT)
    fun depositFromCoinbase(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("payment_method_id") paymentMethodId: String
    ): Call<DepositFromCoinbaseResponse>

    @POST(PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT_ADDRESS)
    fun generateAddressForDeposit(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("coinbaseAccountId") coinbaseAccountId: String
    ): Call<GenerateAddressForDepositResponse>

    @POST(PATH_PRIVATE_POST_WITHDRAWAL_PAYMENT_METHOD)
    fun withdrawToPaymentMethod(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("payment_method_id") paymentMethodId: String
    ): Call<WithdrawToPaymentMethodResponse>

    @POST(PATH_PRIVATE_POST_WITHDRAWAL_COINBASE_ACCOUNT)
    fun withdrawToCoinbase(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("coinbase_account_id") coinbaseAccountId: String
    ): Call<WithdrawToCoinbaseResponse>

    @POST(PATH_PRIVATE_POST_WITHDRAWAL_CRYPTO)
    fun withdrawToCrypto(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("amount") amount: String,
        @Query("currency") currency: String,
        @Query("crypto_address") cryptoAddress: String,
        @Query("destination_tag") destinationTag: String?,
        @Query("no_destination_tag") noDestinationTag: String?,
        @Query("add_network_fee_to_total") addNetworkFeeToTotal: String?
    ): Call<WithdrawToCryptoResponse>

    @GET(PATH_PRIVATE_GET_WITHDRAWAL_FEE_ESTIMATE)
    fun getFeeEstimateForAddress(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("currency") currency: String,
        @Query("crypto_address") cryptoAddress: String
    ): Call<FeeEstimateForAddressResponse>

    @POST(PATH_PRIVATE_POST_CONVERSIONS)
    fun convert(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): Call<ConversionResponse>

    @GET(PATH_PRIVATE_GET_PAYMENT_METHODS)
    fun getPaymentMethods(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<PaymentMethodsResponse>

    @GET(PATH_PRIVATE_GET_COINBASE_ACCOUNTS)
    fun getCoinbaseAccounts(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<CoinbaseAccountsResponse>

    @GET(PATH_PRIVATE_GET_FEES)
    fun getFees(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<FeesResponse>

    @POST(PATH_PRIVATE_POST_REPORTS)
    fun generateReport(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("type") type: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("product_id") productId: String?,
        @Query("account_id") accountId: String?,
        @Query("format") format: String?,
        @Query("email") email: String?
    ): Call<ReportResponse>

    @GET(PATH_PRIVATE_GET_REPORT)
    fun getReport(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("reportId") reportId: String
    ): Call<ReportResponse>

    @GET(PATH_PRIVATE_GET_PROFILES)
    fun getProfiles(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("active") active: Boolean?
    ): Call<ProfilesResponse>

    @GET(PATH_PRIVATE_GET_PROFILE)
    fun getProfile(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Path("profileId") profileId: String
    ): Call<ProfilesResponseItem>

    @POST(PATH_PRIVATE_POST_PROFILES_TRANSFER_FUNDS)
    fun transferBetweenProfiles(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("currency") currency: String,
        @Query("amount") amount: String
    ): Call<String>

    @GET(PATH_PRIVATE_GET_USER_TRAILING_VOLUME)
    fun getUserTrailingVolume(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<UserTrailingVolumeResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_PROFILE_INFORMATION)
    fun getMarginInformation(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("product_id") productId: String
    ): Call<MarginInformationResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_BUYING_POWER)
    fun getMarginBuyingPower(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("product_id") productId: String
    ): Call<MarginBuyingPowerResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER)
    fun getMarginWithdrawalPower(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("currency") currency: String
    ): Call<MarginWithdrawalPowerResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER_ALL)
    fun getMarginAllWithdrawalPowers(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<MarginAllWithdrawalPowersResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_EXIT_PLAN)
    fun getMarginExitPlan(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<MarginExitPlanResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_LIQUIDATION_HISTORY)
    fun getMarginLiquidationHistory(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String,
        @Query("after") after: String?
    ): Call<MarginLiquidationHistoryResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_POSITION_REFRESH_AMOUNTS)
    fun getMarginPositionRefreshAmounts(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<MarginPositionRefreshAmountsResponse>

    @GET(PATH_PRIVATE_GET_MARGIN_STATUS)
    fun getMarginStatus(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<MarginStatusResponse>

    @GET(PATH_PRIVATE_GET_ORACLE)
    fun getOracle(
        @Header(HEADER_KEY) key: String,
        @Header(HEADER_PASSPHRASE) passphrase: String
    ): Call<OracleResponse>

    /*
     * Public API
     */

    @GET(PATH_PUBLIC_GET_PRODUCTS)
    fun getProducts(): Call<ProductsResponse>

    @GET(PATH_PUBLIC_GET_PRODUCT)
    fun getProduct(@Path("productId") productId: String): Call<ProductResponse>

    @GET(PATH_PUBLIC_GET_PRODUCT_BOOK)
    fun getProductBook(
        @Path("productId") productId: String,
        @Query("level") level: Int?
    ): Call<ProductBookResponse>

    @GET(PATH_PUBLIC_GET_PRODUCT_TICKER)
    fun getProductTicker(@Path("productId") productId: String): Call<ProductTickerResponse>

    @GET(PATH_PUBLIC_GET_PRODUCT_TRADES)
    fun getProductTrades(@Path("productId") productId: String): Call<ProductTradesResponse>

    @GET(PATH_PUBLIC_GET_PRODUCT_CANDLES)
    fun getProductCandles(
        @Path("productId") productId: String,
        @Query("start") start: String?,
        @Query("end") end: String?,
        @Query("granularity") granularity: Int?
    ): Call<List<List<Double>>>

    @GET(PATH_PUBLIC_GET_PRODUCT_STATS)
    fun getProductStats(@Path("productId") productId: String): Call<ProductStatsResponse>

    @GET(PATH_PUBLIC_GET_CURRENCIES)
    fun getCurrencies(): Call<CurrenciesResponse>

    @GET(PATH_PUBLIC_GET_TIME)
    fun getTime(): Call<TimeResponse>

}
