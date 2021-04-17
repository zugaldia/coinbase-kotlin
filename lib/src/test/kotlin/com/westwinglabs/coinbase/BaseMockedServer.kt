package com.westwinglabs.coinbase

import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_DELETE_ORDER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_DELETE_ORDERS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_DELETE_ORDER_CLIENT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ACCOUNT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ACCOUNTS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ACCOUNT_HOLDS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ACCOUNT_LEDGER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_COINBASE_ACCOUNTS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_FEES
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_FILLS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_BUYING_POWER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_EXIT_PLAN
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_LIQUIDATION_HISTORY
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_POSITION_REFRESH_AMOUNTS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_PROFILE_INFORMATION
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_STATUS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER_ALL
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ORACLE
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ORDER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ORDERS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_ORDER_CLIENT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_PAYMENT_METHODS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_PROFILE
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_PROFILES
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_REPORT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_TRANSFER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_TRANSFERS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_USER_EXCHANGE_LIMITS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_USER_TRAILING_VOLUME
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_GET_WITHDRAWAL_FEE_ESTIMATE
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_CONVERSIONS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT_ADDRESS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_DEPOSIT_PAYMENT_METHOD
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_ORDERS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_PROFILES_TRANSFER_FUNDS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_REPORTS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_WITHDRAWAL_COINBASE_ACCOUNT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_WITHDRAWAL_CRYPTO
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PRIVATE_POST_WITHDRAWAL_PAYMENT_METHOD
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_CURRENCIES
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCT
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCTS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCT_BOOK
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCT_CANDLES
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCT_STATS
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCT_TICKER
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_PRODUCT_TRADES
import com.westwinglabs.coinbase.service.CoinbaseService.Companion.PATH_PUBLIC_GET_TIME
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before

open class BaseMockedServer {

    companion object {
        const val PRODUCT_VALID = "ETH-BTC"
        const val PRODUCT_INVALID = "XXX-YYY"
        const val SAMPLE_UUID = "132fb6ae-456b-4654-b4e0-d681ac05cea1"
    }

    private lateinit var server: MockWebServer
    internal lateinit var mockedClient: CoinbaseClient

    fun loadFixture(path: String): String =
        IOUtils.resourceToString("/fixtures/$path", Charsets.UTF_8)

    @Before
    fun setup() {
        server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val response: String = when {
                    // Private API
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_ACCOUNTS ->
                        loadFixture("private/get_accounts.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_ACCOUNT.replace("{accountId}", SAMPLE_UUID) ->
                        loadFixture("private/get_account.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_ACCOUNT_LEDGER.replace("{accountId}", SAMPLE_UUID) ->
                        loadFixture("private/get_account_ledger.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_ACCOUNT_HOLDS.replace("{accountId}", SAMPLE_UUID) ->
                        loadFixture("private/get_account_holds.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_ORDERS) ->
                        loadFixture("private/post_orders.json")
                    request.method == "DELETE" && request.path ==
                            PATH_PRIVATE_DELETE_ORDER.replace("{id}", SAMPLE_UUID) ->
                        loadFixture("private/delete_order.json")
                    request.method == "DELETE" && request.path ==
                            PATH_PRIVATE_DELETE_ORDER_CLIENT.replace("{clientOid}", SAMPLE_UUID) ->
                        loadFixture("private/delete_order_by_client.json")
                    request.method == "DELETE" && request.path == PATH_PRIVATE_DELETE_ORDERS ->
                        loadFixture("private/delete_orders.json")
                    request.method == "GET" && request.path == "$PATH_PRIVATE_GET_ORDERS?status=all" ->
                        loadFixture("private/get_orders.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_ORDER.replace("{id}", SAMPLE_UUID) ->
                        loadFixture("private/get_order.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_ORDER_CLIENT.replace("{clientOid}", SAMPLE_UUID) ->
                        loadFixture("private/get_order_by_client.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_FILLS ->
                        loadFixture("private/get_fills.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_USER_EXCHANGE_LIMITS ->
                        loadFixture("private/get_user_exchange_limits.json")
                    request.method == "GET" && request.path == "$PATH_PRIVATE_GET_TRANSFERS?type=deposit" ->
                        // we also have get_withdraws.json
                        loadFixture("private/get_deposits.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_TRANSFER.replace("{transferId}", SAMPLE_UUID) ->
                        // we also have get_withdraw.json
                        loadFixture("private/get_deposit.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_DEPOSIT_PAYMENT_METHOD) ->
                        loadFixture("private/post_deposit_from_payment.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT) ->
                        loadFixture("private/post_deposit_from_coinbase.json")
                    request.method == "POST" && request.path ==
                            PATH_PRIVATE_POST_DEPOSIT_COINBASE_ACCOUNT_ADDRESS.replace("{coinbaseAccountId}", SAMPLE_UUID) ->
                        loadFixture("private/post_deposit_from_coinbase_address.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_WITHDRAWAL_PAYMENT_METHOD) ->
                        loadFixture("private/post_withdraw_to_payment.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_WITHDRAWAL_COINBASE_ACCOUNT) ->
                        loadFixture("private/post_withdraw_to_coinbase.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_WITHDRAWAL_CRYPTO) ->
                        loadFixture("private/post_withdraw_to_crypto.json")
                    request.method == "GET" && request.path.startsWith(PATH_PRIVATE_GET_WITHDRAWAL_FEE_ESTIMATE) ->
                        loadFixture("private/get_withdrawals_fee_estimate.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_CONVERSIONS) ->
                        loadFixture("private/post_conversions.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_PAYMENT_METHODS ->
                        loadFixture("private/get_payment_methods.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_COINBASE_ACCOUNTS ->
                        loadFixture("private/get_coinbase_accounts.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_FEES ->
                        loadFixture("private/get_fees.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_REPORTS) ->
                        loadFixture("private/post_reports_fills.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_REPORT.replace("{reportId}", SAMPLE_UUID) ->
                        loadFixture("private/get_report_fills.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_PROFILES ->
                        loadFixture("private/get_profiles.json")
                    request.method == "GET" && request.path ==
                            PATH_PRIVATE_GET_PROFILE.replace("{profileId}", SAMPLE_UUID) ->
                        loadFixture("private/get_profile.json")
                    request.method == "POST" && request.path.startsWith(PATH_PRIVATE_POST_PROFILES_TRANSFER_FUNDS) ->
                        loadFixture("private/post_profile_transfer.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_USER_TRAILING_VOLUME ->
                        loadFixture("private/get_user_trailing_volume.json")
                    request.method == "GET" && request.path.startsWith(PATH_PRIVATE_GET_MARGIN_PROFILE_INFORMATION) ->
                        loadFixture("private/get_margin_profile_information.json")
                    request.method == "GET" && request.path.startsWith(PATH_PRIVATE_GET_MARGIN_BUYING_POWER) ->
                        loadFixture("private/get_margin_buying_power.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER_ALL ->
                        loadFixture("private/get_margin_withdrawal_power_all.json")
                    request.method == "GET" && request.path.startsWith(PATH_PRIVATE_GET_MARGIN_WITHDRAWAL_POWER) ->
                        loadFixture("private/get_margin_withdrawal_power.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_MARGIN_EXIT_PLAN ->
                        loadFixture("private/get_margin_exit_plan.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_MARGIN_LIQUIDATION_HISTORY ->
                        loadFixture("private/get_margin_liquidation_history.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_MARGIN_POSITION_REFRESH_AMOUNTS ->
                        loadFixture("private/get_margin_position_refresh_amounts.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_MARGIN_STATUS ->
                        loadFixture("private/get_margin_status.json")
                    request.method == "GET" && request.path == PATH_PRIVATE_GET_ORACLE ->
                        loadFixture("private/get_oracle.json")

                    // Public API
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCTS ->
                        loadFixture("public/get_products.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT.replace(
                        "{productId}",
                        PRODUCT_VALID
                    ) ->
                        loadFixture("public/get_product.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT.replace(
                        "{productId}",
                        PRODUCT_INVALID
                    ) ->
                        // there's also one in the private folder (get_error)
                        loadFixture("public/get_error.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT_BOOK.replace(
                        "{productId}",
                        PRODUCT_VALID
                    ) ->
                        loadFixture("public/get_product_book.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT_TICKER.replace(
                        "{productId}",
                        PRODUCT_VALID
                    ) ->
                        loadFixture("public/get_product_ticker.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT_TRADES.replace(
                        "{productId}",
                        PRODUCT_VALID
                    ) ->
                        loadFixture("public/get_product_trades.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT_CANDLES.replace(
                        "{productId}",
                        PRODUCT_VALID
                    ) ->
                        loadFixture("public/get_product_candles.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_PRODUCT_STATS.replace(
                        "{productId}",
                        PRODUCT_VALID
                    ) ->
                        loadFixture("public/get_product_stats.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_CURRENCIES ->
                        loadFixture("public/get_currencies.json")
                    request.method == "GET" && request.path == PATH_PUBLIC_GET_TIME ->
                        loadFixture("public/get_time.json")

                    // Unsupported
                    else -> throw RuntimeException("Unsupported path: ${request.path}")
                }

                val code = when (request.path) {
                    PATH_PUBLIC_GET_PRODUCT.replace("{productId}", PRODUCT_INVALID) -> 404
                    else -> 200
                }

                return MockResponse().setBody(response).setResponseCode(code)
            }
        }

        server.start()
        mockedClient = CoinbaseClient(
            apiKey = SAMPLE_UUID,
            apiPassphrase = SAMPLE_UUID,
            apiSecret = SAMPLE_UUID,
            apiEndpoint = server.url("").toString()
        )
    }

    @After
    fun teardown() {
        server.shutdown()
    }
}