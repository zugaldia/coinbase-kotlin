package com.westwinglabs.coinbase

import com.westwinglabs.coinbase.auth.HeaderInterceptor
import com.westwinglabs.coinbase.service.*
import com.westwinglabs.coinbase.websocket.CoinbaseFeed
import com.westwinglabs.coinbase.websocket.FeedListener
import com.westwinglabs.coinbase.websocket.SubscribeRequest
import com.westwinglabs.coinbase.websocket.UnsubscribeRequest
import com.westwinglabs.coinbase.websocket.SubscribeRequestAuthenticated
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import retrofit2.Retrofit
import java.io.Closeable

class CoinbaseClient(
    private val apiKey: String = "",
    private val apiSecret: String = "",
    private val apiPassphrase: String = "",
    private val appId: String = "com.westwinglabs.coinbase",
    private val apiEndpoint: String = ENDPOINT_PRODUCTION,
    private val feedEndpoint: String = WEBSOCKET_PRODUCTION,
    private val okHttpClient: OkHttpClient = OkHttpClient()
) : Closeable {

    companion object {
        const val ENDPOINT_SANDBOX = "https://api-public.sandbox.pro.coinbase.com"
        const val ENDPOINT_PRODUCTION = "https://api.pro.coinbase.com"

        const val WEBSOCKET_SANDBOX = "wss://ws-feed-public.sandbox.pro.coinbase.com"
        const val WEBSOCKET_PRODUCTION = "wss://ws-feed.pro.coinbase.com"

        const val CHANNEL_HEARTBEAT = "heartbeat"
        const val CHANNEL_STATUS = "status"
        const val CHANNEL_TICKER = "ticker"
        const val CHANNEL_LEVEL2 = "level2"
        const val CHANNEL_USER = "user"
        const val CHANNEL_MATCHES = "matches"
        const val CHANNEL_FULL = "full"
    }

    private val service: CoinbaseService

    private lateinit var feed: CoinbaseFeed
    private lateinit var webSocket: WebSocket

    init {
        check(appId.isNotBlank()) { "App ID cannot be empty." }
        check(apiEndpoint.isNotBlank()) { "API endpoint cannot be empty." }
        check(feedEndpoint.isNotBlank()) { "Websocket endpoint cannot be empty." }

        // Introduce the header interceptor at the beginning of the chain so that it
        // plays nicely with any logging interceptors included with the app.
        val clientWithSigning = okHttpClient.newBuilder()
        clientWithSigning.interceptors().add(0, HeaderInterceptor(apiSecret, appId))

        val retrofit = Retrofit.Builder()
            .client(clientWithSigning.build())
            .baseUrl(apiEndpoint)
            .addConverterFactory(CoinbaseConverterFactory.create())
            .build()

        service = retrofit.create(CoinbaseService::class.java)
    }

    /**
     * Shutdown isn't necessary. The threads and connections that are held will be released automatically if they
     * remain idle. But if you are writing an application that needs to aggressively release unused resources you
     * may do so by calling this function.
     */
    override fun close() {
        // https://github.com/square/okhttp/issues/4029
        // https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
        okHttpClient.dispatcher().executorService().shutdown()
        okHttpClient.connectionPool().evictAll()
        okHttpClient.cache()?.close()
    }

    /*
     * DateTime management
     */

    private val dateTimeFormatter = ISODateTimeFormat.dateTime().withZoneUTC()

    fun formatDateTime(dateTime: DateTime?): String? =
        if (dateTime == null) null else dateTimeFormatter.print(dateTime)

    fun parseDateTime(encoded: String): DateTime = dateTimeFormatter.parseDateTime(encoded)

    /*
     * === Private API ===
     */

    fun getAccounts(): AccountsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getAccounts(apiKey, apiPassphrase).execute().body()

    fun getAccounts(callback: CoinbaseCallback<AccountsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getAccounts(apiKey, apiPassphrase).enqueueWith(callback)

    fun getAccount(accountId: String): AccountResponseItem? =
        service.validatePrivate(apiKey, apiPassphrase).getAccount(apiKey, apiPassphrase, accountId).execute().body()

    fun getAccount(accountId: String, callback: CoinbaseCallback<AccountResponseItem>) =
        service.validatePrivate(apiKey, apiPassphrase).getAccount(apiKey, apiPassphrase, accountId)
            .enqueueWith(callback)

    fun getAccountLedger(accountId: String): AccountLedgerResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getAccountLedger(apiKey, apiPassphrase, accountId).execute()
            .body()

    fun getAccountLedger(accountId: String, callback: CoinbaseCallback<AccountLedgerResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getAccountLedger(apiKey, apiPassphrase, accountId)
            .enqueueWith(callback)

    fun getAccountHolds(accountId: String): AccountHoldsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getAccountHolds(apiKey, apiPassphrase, accountId).execute()
            .body()

    fun getAccountHolds(accountId: String, callback: CoinbaseCallback<AccountHoldsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getAccountHolds(apiKey, apiPassphrase, accountId)
            .enqueueWith(callback)

    fun postOrder(
        side: String,
        productId: String,
        price: String,
        size: String,
        clientOid: String? = null,
        type: String? = null,
        stp: String? = null,
        stop: String? = null,
        stopPrice: String? = null,
        timeInForce: String? = null,
        cancelAfter: String? = null,
        postOnly: String? = null,
        funds: String? = null
    ): PostOrderResponse? =
        service.validatePrivate(apiKey, apiPassphrase).postOrder(
            apiKey,
            apiPassphrase,
            side,
            productId,
            price,
            size,
            clientOid,
            type,
            stp,
            stop,
            stopPrice,
            timeInForce,
            cancelAfter,
            postOnly,
            funds
        ).execute().body()

    fun postOrder(
        side: String,
        productId: String,
        price: String,
        size: String,
        clientOid: String? = null,
        type: String? = null,
        stp: String? = null,
        stop: String? = null,
        stopPrice: String? = null,
        timeInForce: String? = null,
        cancelAfter: String? = null,
        postOnly: String? = null,
        funds: String? = null,
        callback: CoinbaseCallback<PostOrderResponse>
    ) =
        service.validatePrivate(apiKey, apiPassphrase).postOrder(
            apiKey,
            apiPassphrase,
            side,
            productId,
            price,
            size,
            clientOid,
            type,
            stp,
            stop,
            stopPrice,
            timeInForce,
            cancelAfter,
            postOnly,
            funds
        ).enqueueWith(callback)

    fun cancelOrder(orderId: String, productId: String? = null): String? =
        service.validatePrivate(apiKey, apiPassphrase).cancelOrder(apiKey, apiPassphrase, orderId, productId).execute()
            .body()

    fun cancelOrder(orderId: String, productId: String? = null, callback: CoinbaseCallback<String>) =
        service.validatePrivate(apiKey, apiPassphrase).cancelOrder(apiKey, apiPassphrase, orderId, productId)
            .enqueueWith(callback)

    fun cancelOrdersByClientId(clientId: String, productId: String? = null): String? =
        service.validatePrivate(apiKey, apiPassphrase)
            .cancelOrdersByClientId(apiKey, apiPassphrase, clientId, productId)
            .execute()
            .body()

    fun cancelOrdersByClientId(clientId: String, productId: String? = null, callback: CoinbaseCallback<String>) =
        service.validatePrivate(apiKey, apiPassphrase)
            .cancelOrdersByClientId(apiKey, apiPassphrase, clientId, productId)
            .enqueueWith(callback)

    fun cancelOrders(productId: String? = null): List<String>? =
        service.validatePrivate(apiKey, apiPassphrase).cancelOrders(apiKey, apiPassphrase, productId).execute().body()

    fun cancelOrders(productId: String? = null, callback: CoinbaseCallback<List<String>>) =
        service.validatePrivate(apiKey, apiPassphrase).cancelOrders(apiKey, apiPassphrase, productId)
            .enqueueWith(callback)

    fun getOrders(status: String = "all", productId: String? = null): OrdersResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getOrders(apiKey, apiPassphrase, status, productId).execute()
            .body()

    fun getOrders(status: String = "all", productId: String? = null, callback: CoinbaseCallback<OrdersResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getOrders(apiKey, apiPassphrase, status, productId)
            .enqueueWith(callback)

    fun getOrder(orderId: String): OrderResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getOrder(apiKey, apiPassphrase, orderId).execute().body()

    fun getOrder(orderId: String, callback: CoinbaseCallback<OrderResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getOrder(apiKey, apiPassphrase, orderId).enqueueWith(callback)

    fun getOrderByClientId(clientId: String): OrderResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getOrderByClientId(apiKey, apiPassphrase, clientId).execute()
            .body()

    fun getOrderByClientId(clientId: String, callback: CoinbaseCallback<OrderResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getOrderByClientId(apiKey, apiPassphrase, clientId)
            .enqueueWith(callback)

    fun getFills(orderId: String? = null, productId: String? = null): FillsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getFills(apiKey, apiPassphrase, orderId, productId).execute()
            .body()

    fun getFills(orderId: String? = null, productId: String? = null, callback: CoinbaseCallback<FillsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getFills(apiKey, apiPassphrase, orderId, productId)
            .enqueueWith(callback)

    fun getExchangeLimits(): ExchangeLimitsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getExchangeLimits(apiKey, apiPassphrase).execute().body()

    fun getExchangeLimits(callback: CoinbaseCallback<ExchangeLimitsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getExchangeLimits(apiKey, apiPassphrase).enqueueWith(callback)

    fun getDeposits(
        productId: String? = null,
        before: DateTime? = null,
        after: DateTime? = null,
        limit: String? = null
    ): TransfersResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .getTransfers(
                apiKey,
                apiPassphrase,
                "deposit",
                productId,
                formatDateTime(before),
                formatDateTime(after),
                limit
            ).execute().body()

    fun getDeposits(
        productId: String? = null,
        before: DateTime? = null,
        after: DateTime? = null,
        limit: String? = null,
        callback: CoinbaseCallback<TransfersResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .getTransfers(apiKey, apiPassphrase, "deposit", productId, formatDateTime(before), formatDateTime(after), limit)
        .enqueueWith(callback)

    fun getTransfer(transferId: String): TransfersResponseItem? =
        service.validatePrivate(apiKey, apiPassphrase).getTransfer(apiKey, apiPassphrase, transferId).execute().body()

    fun getTransfer(transferId: String, callback: CoinbaseCallback<TransfersResponseItem>) =
        service.validatePrivate(apiKey, apiPassphrase).getTransfer(apiKey, apiPassphrase, transferId)
            .enqueueWith(callback)

    fun depositFromPaymentMethod(
        amount: String,
        currency: String,
        paymentMethodId: String
    ): DepositFromPaymentMethodResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .depositFromPaymentMethod(apiKey, apiPassphrase, amount, currency, paymentMethodId).execute().body()

    fun depositFromPaymentMethod(
        amount: String,
        currency: String,
        paymentMethodId: String,
        callback: CoinbaseCallback<DepositFromPaymentMethodResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .depositFromPaymentMethod(apiKey, apiPassphrase, amount, currency, paymentMethodId)
        .enqueueWith(callback)

    fun depositFromCoinbase(amount: String, currency: String, paymentMethodId: String): DepositFromCoinbaseResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .depositFromCoinbase(apiKey, apiPassphrase, amount, currency, paymentMethodId).execute().body()

    fun depositFromCoinbase(
        amount: String,
        currency: String,
        paymentMethodId: String,
        callback: CoinbaseCallback<DepositFromCoinbaseResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .depositFromCoinbase(apiKey, apiPassphrase, amount, currency, paymentMethodId).enqueueWith(callback)

    fun generateAddressForDeposit(coinbaseAccountId: String): GenerateAddressForDepositResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .generateAddressForDeposit(apiKey, apiPassphrase, coinbaseAccountId).execute()
            .body()

    fun generateAddressForDeposit(
        coinbaseAccountId: String,
        callback: CoinbaseCallback<GenerateAddressForDepositResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .generateAddressForDeposit(apiKey, apiPassphrase, coinbaseAccountId)
        .enqueueWith(callback)

    fun getWithdrawals(
        profileId: String? = null,
        before: DateTime? = null,
        after: DateTime? = null,
        limit: String? = null
    ): TransfersResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .getTransfers(
                apiKey,
                apiPassphrase,
                "withdraw",
                profileId,
                formatDateTime(before),
                formatDateTime(after),
                limit
            ).execute().body()

    fun getWithdrawals(
        profileId: String? = null,
        before: DateTime? = null,
        after: DateTime? = null,
        limit: String? = null,
        callback: CoinbaseCallback<TransfersResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .getTransfers(
            apiKey,
            apiPassphrase,
            "withdraw",
            profileId,
            formatDateTime(before),
            formatDateTime(after),
            limit
        ).enqueueWith(callback)

    fun withdrawToPaymentMethod(
        amount: String,
        currency: String,
        paymentMethodId: String
    ): WithdrawToPaymentMethodResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .withdrawToPaymentMethod(apiKey, apiPassphrase, amount, currency, paymentMethodId).execute().body()

    fun withdrawToPaymentMethod(
        amount: String,
        currency: String,
        paymentMethodId: String,
        callback: CoinbaseCallback<WithdrawToPaymentMethodResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .withdrawToPaymentMethod(apiKey, apiPassphrase, amount, currency, paymentMethodId)
        .enqueueWith(callback)

    fun withdrawToCoinbase(
        amount: String,
        currency: String,
        coinbaseAccountId: String
    ): WithdrawToCoinbaseResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .withdrawToCoinbase(apiKey, apiPassphrase, amount, currency, coinbaseAccountId).execute().body()

    fun withdrawToCoinbase(
        amount: String,
        currency: String,
        coinbaseAccountId: String,
        callback: CoinbaseCallback<WithdrawToCoinbaseResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .withdrawToCoinbase(apiKey, apiPassphrase, amount, currency, coinbaseAccountId).enqueueWith(callback)

    fun withdrawToCrypto(
        amount: String,
        currency: String,
        cryptoAddress: String,
        destinationTag: String? = null,
        noDestinationTag: String? = null,
        addNetworkFeeToTotal: String? = null
    ): WithdrawToCryptoResponse? =
        service.validatePrivate(apiKey, apiPassphrase).withdrawToCrypto(
            apiKey,
            apiPassphrase,
            amount,
            currency,
            cryptoAddress,
            destinationTag,
            noDestinationTag,
            addNetworkFeeToTotal
        ).execute().body()

    fun withdrawToCrypto(
        amount: String,
        currency: String,
        cryptoAddress: String,
        destinationTag: String? = null,
        noDestinationTag: String? = null,
        addNetworkFeeToTotal: String? = null,
        callback: CoinbaseCallback<WithdrawToCryptoResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase).withdrawToCrypto(
        apiKey,
        apiPassphrase,
        amount,
        currency,
        cryptoAddress,
        destinationTag,
        noDestinationTag,
        addNetworkFeeToTotal
    ).enqueueWith(callback)

    fun getFeeEstimateForAddress(currency: String, cryptoAddress: String): FeeEstimateForAddressResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .getFeeEstimateForAddress(apiKey, apiPassphrase, currency, cryptoAddress).execute().body()

    fun getFeeEstimateForAddress(
        currency: String,
        cryptoAddress: String,
        callback: CoinbaseCallback<FeeEstimateForAddressResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .getFeeEstimateForAddress(apiKey, apiPassphrase, currency, cryptoAddress)
        .enqueueWith(callback)

    fun convert(from: String, to: String, amount: String): ConversionResponse? =
        service.validatePrivate(apiKey, apiPassphrase).convert(apiKey, apiPassphrase, from, to, amount).execute().body()

    fun convert(from: String, to: String, amount: String, callback: CoinbaseCallback<ConversionResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).convert(apiKey, apiPassphrase, from, to, amount)
            .enqueueWith(callback)

    fun getPaymentMethods(): PaymentMethodsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getPaymentMethods(apiKey, apiPassphrase).execute().body()

    fun getPaymentMethods(callback: CoinbaseCallback<PaymentMethodsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getPaymentMethods(apiKey, apiPassphrase).enqueueWith(callback)

    fun getCoinbaseAccounts(): CoinbaseAccountsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getCoinbaseAccounts(apiKey, apiPassphrase).execute().body()

    fun getCoinbaseAccounts(callback: CoinbaseCallback<CoinbaseAccountsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getCoinbaseAccounts(apiKey, apiPassphrase).enqueueWith(callback)

    fun getFees(): FeesResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getFees(apiKey, apiPassphrase).execute().body()

    fun getFees(callback: CoinbaseCallback<FeesResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getFees(apiKey, apiPassphrase).enqueueWith(callback)

    fun generateReport(
        type: String = "fills",
        startDate: String,
        endDate: String,
        productId: String? = null,
        accountId: String? = null,
        format: String? = null,
        email: String? = null
    ): ReportResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .generateReport(apiKey, apiPassphrase, type, startDate, endDate, productId, accountId, format, email)
            .execute()
            .body()

    fun generateReport(
        type: String = "fills",
        startDate: String,
        endDate: String,
        productId: String? = null,
        accountId: String? = null,
        format: String? = null,
        email: String? = null,
        callback: CoinbaseCallback<ReportResponse>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .generateReport(apiKey, apiPassphrase, type, startDate, endDate, productId, accountId, format, email)
        .enqueueWith(callback)

    fun getReport(reportId: String): ReportResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getReport(apiKey, apiPassphrase, reportId).execute()
            .body()

    fun getReport(reportId: String, callback: CoinbaseCallback<ReportResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getReport(apiKey, apiPassphrase, reportId)
            .enqueueWith(callback)

    fun getProfiles(active: Boolean? = null): ProfilesResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getProfiles(apiKey, apiPassphrase, active).execute().body()

    fun getProfiles(active: Boolean? = null, callback: CoinbaseCallback<ProfilesResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getProfiles(apiKey, apiPassphrase, active).enqueueWith(callback)

    fun getProfile(profileId: String): ProfilesResponseItem? =
        service.validatePrivate(apiKey, apiPassphrase).getProfile(apiKey, apiPassphrase, profileId).execute().body()

    fun getProfile(profileId: String, callback: CoinbaseCallback<ProfilesResponseItem>) =
        service.validatePrivate(apiKey, apiPassphrase).getProfile(apiKey, apiPassphrase, profileId)
            .enqueueWith(callback)

    fun transferBetweenProfiles(
        from: String,
        to: String,
        currency: String,
        amount: String
    ): String? =
        service.validatePrivate(apiKey, apiPassphrase)
            .transferBetweenProfiles(apiKey, apiPassphrase, from, to, currency, amount).execute().body()

    fun transferBetweenProfiles(
        from: String,
        to: String,
        currency: String,
        amount: String,
        callback: CoinbaseCallback<String>
    ) = service.validatePrivate(apiKey, apiPassphrase)
        .transferBetweenProfiles(apiKey, apiPassphrase, from, to, currency, amount)
        .enqueueWith(callback)

    fun getUserTrailingVolume(): UserTrailingVolumeResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getUserTrailingVolume(apiKey, apiPassphrase).execute().body()

    fun getUserTrailingVolume(callback: CoinbaseCallback<UserTrailingVolumeResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getUserTrailingVolume(apiKey, apiPassphrase)
            .enqueueWith(callback)

    fun getMarginInformation(productId: String): MarginInformationResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginInformation(apiKey, apiPassphrase, productId).execute()
            .body()

    fun getMarginInformation(productId: String, callback: CoinbaseCallback<MarginInformationResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginInformation(apiKey, apiPassphrase, productId)
            .enqueueWith(callback)

    fun getMarginBuyingPower(productId: String): MarginBuyingPowerResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginBuyingPower(apiKey, apiPassphrase, productId).execute()
            .body()

    fun getMarginBuyingPower(productId: String, callback: CoinbaseCallback<MarginBuyingPowerResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginBuyingPower(apiKey, apiPassphrase, productId)
            .enqueueWith(callback)

    fun getMarginWithdrawalPower(currency: String): MarginWithdrawalPowerResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginWithdrawalPower(apiKey, apiPassphrase, currency)
            .execute().body()

    fun getMarginWithdrawalPower(currency: String, callback: CoinbaseCallback<MarginWithdrawalPowerResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginWithdrawalPower(apiKey, apiPassphrase, currency)
            .enqueueWith(callback)

    fun getMarginAllWithdrawalPowers(): MarginAllWithdrawalPowersResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginAllWithdrawalPowers(apiKey, apiPassphrase).execute()
            .body()

    fun getMarginAllWithdrawalPowers(callback: CoinbaseCallback<MarginAllWithdrawalPowersResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginAllWithdrawalPowers(apiKey, apiPassphrase)
            .enqueueWith(callback)

    fun getMarginExitPlan(): MarginExitPlanResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginExitPlan(apiKey, apiPassphrase).execute().body()

    fun getMarginExitPlan(callback: CoinbaseCallback<MarginExitPlanResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginExitPlan(apiKey, apiPassphrase).enqueueWith(callback)

    fun getMarginLiquidationHistory(after: DateTime? = null): MarginLiquidationHistoryResponse? =
        service.validatePrivate(apiKey, apiPassphrase)
            .getMarginLiquidationHistory(apiKey, apiPassphrase, formatDateTime(after))
            .execute()
            .body()

    fun getMarginLiquidationHistory(
        after: DateTime? = null,
        callback: CoinbaseCallback<MarginLiquidationHistoryResponse>
    ) =
        service.validatePrivate(apiKey, apiPassphrase)
            .getMarginLiquidationHistory(apiKey, apiPassphrase, formatDateTime(after))
            .enqueueWith(callback)

    fun getMarginPositionRefreshAmounts(): MarginPositionRefreshAmountsResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginPositionRefreshAmounts(apiKey, apiPassphrase).execute()
            .body()

    fun getMarginPositionRefreshAmounts(callback: CoinbaseCallback<MarginPositionRefreshAmountsResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginPositionRefreshAmounts(apiKey, apiPassphrase)
            .enqueueWith(callback)

    fun getMarginStatus(): MarginStatusResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getMarginStatus(apiKey, apiPassphrase).execute().body()

    fun getMarginStatus(callback: CoinbaseCallback<MarginStatusResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getMarginStatus(apiKey, apiPassphrase).enqueueWith(callback)

    fun getOracle(): OracleResponse? =
        service.validatePrivate(apiKey, apiPassphrase).getOracle(apiKey, apiPassphrase).execute().body()

    fun getOracle(callback: CoinbaseCallback<OracleResponse>) =
        service.validatePrivate(apiKey, apiPassphrase).getOracle(apiKey, apiPassphrase).enqueueWith(callback)

    /*
     * === Public API ===
     *
     * The Market Data API is an unauthenticated set of endpoints for retrieving market data. These endpoints provide
     * snapshots of market data.
     */

    /**
     * Get a list of available currency pairs for trading.
     */
    fun getProducts(): ProductsResponse? =
        service.getProducts().execute().body()

    /**
     * Get a list of available currency pairs for trading asynchronously.
     *
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProducts(callback: CoinbaseCallback<ProductsResponse>) =
        service.getProducts().enqueueWith(callback)

    /**
     * Get market data for a specific currency pair.
     *
     * @param productId the currency pair (example: BTC-USD)
     */
    fun getProduct(productId: String): ProductResponse? =
        service.getProduct(productId).execute().body()

    /**
     * Get market data for a specific currency pair asynchronously.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProduct(productId: String, callback: CoinbaseCallback<ProductResponse>) =
        service.getProduct(productId).enqueueWith(callback)

    /**
     * Get a list of open orders for a product. The amount of detail shown can be customized with the level parameter.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param level Select response detail. Valid values are 1 (Only the best bid and ask), 2 (Top 50 bids and asks
     *              aggregated), and 3 (Full order book non aggregated)
     */
    fun getProductBook(productId: String, level: Int? = null): ProductBookResponse? =
        service.getProductBook(productId, level).execute().body()

    /**
     * Get a list of open orders for a product asynchronously. The amount of detail shown can be customized with the
     * level parameter.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param level Select response detail. Valid values are 1 (Only the best bid and ask), 2 (Top 50 bids and asks
     *              aggregated), and 3 (Full order book non aggregated)
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProductBook(productId: String, level: Int? = null, callback: CoinbaseCallback<ProductBookResponse>) =
        service.getProductBook(productId, level).enqueueWith(callback)

    /**
     * Snapshot information about the last trade (tick), best bid/ask and 24h volume.
     *
     * @param productId the currency pair (example: BTC-USD)
     */
    fun getProductTicker(productId: String): ProductTickerResponse? =
        service.getProductTicker(productId).execute().body()

    /**
     * Snapshot information about the last trade (tick), best bid/ask and 24h volume, asynchronously.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProductTicker(productId: String, callback: CoinbaseCallback<ProductTickerResponse>) =
        service.getProductTicker(productId).enqueueWith(callback)

    /**
     * List the latest trades for a product.
     *
     * @param productId the currency pair (example: BTC-USD)
     */
    fun getProductTrades(productId: String): ProductTradesResponse? =
        service.getProductTrades(productId).execute().body()

    /**
     * List the latest trades for a product asynchronously.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProductTrades(productId: String, callback: CoinbaseCallback<ProductTradesResponse>) =
        service.getProductTrades(productId).enqueueWith(callback)

    /**
     * Historic rates for a product. Rates are returned in grouped buckets based on requested granularity.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param start Start time in ISO 8601
     * @param end End time in ISO 8601
     * @param granularity Desired timeslice in seconds (60, 300, 900, 3600, 21600, or 86400)
     */
    fun getProductCandles(
        productId: String,
        start: DateTime? = null,
        end: DateTime? = null,
        granularity: Int? = null
    ): List<List<Double>>? =
        service.getProductCandles(productId, formatDateTime(start), formatDateTime(end), granularity).execute().body()

    /**
     * Historic rates for a product. Rates are returned in grouped buckets based on requested granularity
     * asynchronously.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param start Start time in ISO 8601
     * @param end End time in ISO 8601
     * @param granularity Desired timeslice in seconds (60, 300, 900, 3600, 21600, or 86400)
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProductCandles(
        productId: String,
        start: DateTime? = null,
        end: DateTime? = null,
        granularity: Int? = null,
        callback: CoinbaseCallback<List<List<Double>>>
    ) = service.getProductCandles(productId, formatDateTime(start), formatDateTime(end), granularity)
        .enqueueWith(callback)

    /**
     * Get 24 hr stats for the product. volume is in base currency units. open, high, low are in quote currency units.
     *
     * @param productId the currency pair (example: BTC-USD)
     */
    fun getProductStats(productId: String): ProductStatsResponse? =
        service.getProductStats(productId).execute().body()

    /**
     * Get 24 hr stats for the product. volume is in base currency units. open, high, low are in quote currency
     * units asynchronously.
     *
     * @param productId the currency pair (example: BTC-USD)
     * @param callback notifies the response or if an error occurred talking to the server.
     */
    fun getProductStats(productId: String, callback: CoinbaseCallback<ProductStatsResponse>) =
        service.getProductStats(productId).enqueueWith(callback)

    /**
     * List known currencies.
     */
    fun getCurrencies(): CurrenciesResponse? =
        service.getCurrencies().execute().body()

    /**
     * List known currencies.
     *
     * @param callback notifies the response or if an error occurred talking to the server asynchronously.
     */
    fun getCurrencies(callback: CoinbaseCallback<CurrenciesResponse>) =
        service.getCurrencies().enqueueWith(callback)

    /**
     * Get the API server time.
     */
    fun getTime(): TimeResponse? =
        service.getTime().execute().body()

    /**
     * Get the API server time.
     *
     * @param callback notifies the response or if an error occurred talking to the server asynchronously.
     */
    fun getTime(callback: CoinbaseCallback<TimeResponse>) =
        service.getTime().enqueueWith(callback)

    /*
     * === Websocket logic ===
     */

    fun openFeed(feedListener: FeedListener) {
        feed = CoinbaseFeed(feedListener)
        val request = Request.Builder().url(feedEndpoint).build()
        webSocket = okHttpClient.newWebSocket(request, feed)
    }

    /**
     * This function attempts to initiate a graceful shutdown of the web socket, it doesn't close the
     * underlying OkHttp client. If you need to do so, which is optional, you need to additionally
     * call {@link #close()}.
     */
    fun closeFeed(code: Int = 1000, reason: String = ""): Boolean = webSocket.close(code, reason)

    fun subscribe(request: SubscribeRequest): Boolean = subscribe(
        encoded = CoinbaseConverterFactory.mapper.writeValueAsString(request)
    )

    fun subscribeAuthenticated(request: SubscribeRequestAuthenticated): Boolean = subscribe(
        encoded = CoinbaseConverterFactory.mapper.writeValueAsString(request)
    )

    private fun subscribe(encoded: String):Boolean {
        return webSocket.send(encoded)
    }

    fun unsubscribe(request: UnsubscribeRequest): Boolean {
        val encoded = CoinbaseConverterFactory.mapper.writeValueAsString(request)
        return webSocket.send(encoded)
    }
}
