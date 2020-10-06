package com.westwinglabs.coinbase.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

class AccountsResponse : ArrayList<AccountResponseItem>()

data class AccountResponseItem(
    @JsonProperty("id") val id: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("balance") val balance: String,
    @JsonProperty("available") val available: String,
    @JsonProperty("hold") val hold: String,
    @JsonProperty("profile_id") val profileId: String,
    @JsonProperty("trading_enabled") val tradingEnabled: Boolean
)

class AccountLedgerResponse : ArrayList<AccountLedgerResponseItem>()

data class AccountLedgerResponseItem(
    @JsonProperty("id") val id: String,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("amount") val amount: String,
    @JsonProperty("balance") val balance: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("details") val details: AccountLedgerResponseItemDetails
)

data class AccountLedgerResponseItemDetails(
    @JsonProperty("order_id") val orderId: String,
    @JsonProperty("trade_id") val tradeId: String,
    @JsonProperty("product_id") val productId: String
)

class AccountHoldsResponse : ArrayList<AccountHoldsResponseItem>()

data class AccountHoldsResponseItem(
    @JsonProperty("id") val id: String,
    @JsonProperty("account_id") val accountId: String,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("updated_at") val updatedAt: String,
    @JsonProperty("amount") val amount: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("ref") val ref: String
)

data class PostOrderResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("price") val price: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("side") val side: String,
    @JsonProperty("stp") val stp: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("time_in_force") val timeInForce: String,
    @JsonProperty("post_only") val postOnly: Boolean,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("fill_fees") val fillFees: String,
    @JsonProperty("filled_size") val filledSize: String,
    @JsonProperty("executed_value") val executedValue: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("settled") val settled: Boolean
)

class OrdersResponse : ArrayList<OrdersResponseItem>()

data class OrdersResponseItem(
    @JsonProperty("id") val id: String,
    @JsonProperty("price") val price: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("profile_id") val profileId: String,
    @JsonProperty("side") val side: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("time_in_force") val timeInForce: String,
    @JsonProperty("post_only") val postOnly: Boolean,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("fill_fees") val fillFees: String,
    @JsonProperty("filled_size") val filledSize: String,
    @JsonProperty("executed_value") val executedValue: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("settled") val settled: Boolean
)

data class OrderResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("side") val side: String,
    @JsonProperty("stp") val stp: String,
    @JsonProperty("funds") val funds: String,
    @JsonProperty("specified_funds") val specifiedFunds: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("post_only") val postOnly: Boolean,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("done_at") val doneAt: String,
    @JsonProperty("done_reason") val doneReason: String,
    @JsonProperty("fill_fees") val fillFees: String,
    @JsonProperty("filled_size") val filledSize: String,
    @JsonProperty("executed_value") val executedValue: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("settled") val settled: Boolean
)

class FillsResponse : ArrayList<FillsResponseItem>()

data class FillsResponseItem(
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("fee") val fee: String,
    @JsonProperty("liquidity") val liquidity: String,
    @JsonProperty("order_id") val orderId: String,
    @JsonProperty("price") val price: String,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("settled") val settled: Boolean,
    @JsonProperty("side") val side: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("trade_id") val tradeId: Int
)

data class ExchangeLimitsResponse(
    @JsonProperty("limit_currency") val limitCurrency: String,
    @JsonProperty("transfer_limits") val transferLimits: JsonNode
)

class TransfersResponse : ArrayList<TransfersResponseItem>()

data class TransfersResponseItem(
    @JsonProperty("account_id") val accountId: String,
    @JsonProperty("amount") val amount: String,
    @JsonProperty("canceled_at") val canceledAt: String?,
    @JsonProperty("completed_at") val completedAt: String,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("details") val details: TransfersResponseItemDetails,
    @JsonProperty("id") val id: String,
    @JsonProperty("processed_at") val processedAt: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("user_id") val userId: String,
    @JsonProperty("user_nonce") val userNonce: String?
)

data class TransfersResponseItemDetails(
    @JsonProperty("coinbase_account_id") val coinbaseAccountId: String?,
    @JsonProperty("coinbase_transaction_id") val coinbaseTransactionId: String?,
    @JsonProperty("coinbase_payment_method_id") val coinbasePaymentMethodId: String?,
    @JsonProperty("crypto_address") val cryptoAddress: String?,
    @JsonProperty("crypto_transaction_hash") val cryptoTransactionHash: String?,
    @JsonProperty("crypto_transaction_id") val cryptoTransactionId: String?,
    @JsonProperty("destination_tag") val destinationTag: String?,
    @JsonProperty("destination_tag_name") val destinationTagName: String?
)

data class DepositFromPaymentMethodResponse(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("payout_at") val payoutAt: String
)

data class DepositFromCoinbaseResponse(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("id") val id: String
)

data class GenerateAddressForDepositResponse(
    @JsonProperty("address") val address: String,
    @JsonProperty("callback_url") val callbackUrl: String?,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("exchange_deposit_address") val exchangeDepositAddress: Boolean,
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("resource") val resource: String,
    @JsonProperty("resource_path") val resourcePath: String,
    @JsonProperty("updated_at") val updatedAt: String
)

data class WithdrawToPaymentMethodResponse(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("payout_at") val payoutAt: String
)

data class WithdrawToCoinbaseResponse(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("id") val id: String
)

data class WithdrawToCryptoResponse(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("fee") val fee: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("subtotal") val subtotal: String
)

data class FeeEstimateForAddressResponse(
    @JsonProperty("fee") val fee: String
)

data class ConversionResponse(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("from") val from: String,
    @JsonProperty("from_account_id") val fromAccountId: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("to") val to: String,
    @JsonProperty("to_account_id") val toAccountId: String
)

class PaymentMethodsResponse : ArrayList<PaymentMethodsResponseItem>()

data class PaymentMethodsResponseItem(
    @JsonProperty("allow_buy") val allowBuy: Boolean,
    @JsonProperty("allow_deposit") val allowDeposit: Boolean,
    @JsonProperty("allow_sell") val allowSell: Boolean,
    @JsonProperty("allow_withdraw") val allowWithdraw: Boolean,
    @JsonProperty("cdv_status") val cdvStatus: String?,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("fiat_account") val fiatAccount: PaymentMethodsResponseFiatAccount?,
    @JsonProperty("hold_business_days") val holdBusinessDays: Int,
    @JsonProperty("hold_days") val holdDays: Int,
    @JsonProperty("id") val id: String,
    @JsonProperty("instant_buy") val instantBuy: Boolean,
    @JsonProperty("instant_sell") val instantSell: Boolean,
    @JsonProperty("limits") val limits: JsonNode,
    @JsonProperty("name") val name: String,
    @JsonProperty("picker_data") val pickerData: PaymentMethodsResponsePickerData?,
    @JsonProperty("primary_buy") val primaryBuy: Boolean,
    @JsonProperty("primary_sell") val primarySell: Boolean,
    @JsonProperty("resource") val resource: String,
    @JsonProperty("resource_path") val resourcePath: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("updated_at") val updatedAt: String,
    @JsonProperty("verification_method") val verificationMethod: String?,
    @JsonProperty("verified") val verified: Boolean
)

data class PaymentMethodsResponseFiatAccount(
    @JsonProperty("id") val id: String,
    @JsonProperty("resource") val resource: String
)

data class PaymentMethodsResponsePickerData(
    @JsonProperty("account_name") val accountName: String,
    @JsonProperty("account_number") val accountNumber: String,
    @JsonProperty("routing_number") val routingNumber: String,
    @JsonProperty("symbol") val symbol: String
)

class CoinbaseAccountsResponse : ArrayList<CoinbaseAccountsResponseItem>()

data class CoinbaseAccountsResponseItem(
    @JsonProperty("active") val active: Boolean,
    @JsonProperty("available_on_consumer") val availableOnConsumer: Boolean,
    @JsonProperty("balance") val balance: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("hold_balance") val holdBalance: String,
    @JsonProperty("hold_currency") val holdCurrency: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("primary") val primary: Boolean,
    @JsonProperty("sepa_deposit_information") val sepaDepositInformation: CoinbaseAccountsResponseSepaDepositInformation?,
    @JsonProperty("type") val type: String,
    @JsonProperty("uk_deposit_information") val ukDepositInformation: CoinbaseAccountsResponseUkDepositInformation?,
    @JsonProperty("wire_deposit_information") val wireDepositInformation: CoinbaseAccountsResponseWireDepositInformation?
)

data class CoinbaseAccountsResponseSepaDepositInformation(
    @JsonProperty("account_address") val accountAddress: String,
    @JsonProperty("account_name") val accountName: String,
    @JsonProperty("bank_address") val bankAddress: String,
    @JsonProperty("bank_country_name") val bankCountryName: String,
    @JsonProperty("bank_name") val bankName: String,
    @JsonProperty("iban") val iban: String,
    @JsonProperty("reference") val reference: String,
    @JsonProperty("swift") val swift: String
)

data class CoinbaseAccountsResponseUkDepositInformation(
    @JsonProperty("account_name") val accountName: String,
    @JsonProperty("account_number") val accountNumber: String,
    @JsonProperty("bank_name") val bankName: String,
    @JsonProperty("reference") val reference: String,
    @JsonProperty("sort_code") val sortCode: String
)

data class CoinbaseAccountsResponseWireDepositInformation(
    @JsonProperty("account_address") val accountAddress: String,
    @JsonProperty("account_name") val accountName: String,
    @JsonProperty("account_number") val accountNumber: String,
    @JsonProperty("bank_address") val bankAddress: String,
    @JsonProperty("bank_country") val bankCountry: CoinbaseAccountsResponseBankCountry,
    @JsonProperty("bank_name") val bankName: String,
    @JsonProperty("reference") val reference: String,
    @JsonProperty("routing_number") val routingNumber: String
)

data class CoinbaseAccountsResponseBankCountry(
    @JsonProperty("code") val code: String,
    @JsonProperty("name") val name: String
)

class FeesResponse(
    @JsonProperty("maker_fee_rate") val makerFeeRate: String,
    @JsonProperty("taker_fee_rate") val takerFeeRate: String,
    @JsonProperty("usd_volume") val usdVolume: String?
)

data class ReportResponse(
    @JsonProperty("completed_at") val completedAt: String?,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("expires_at") val expiresAt: String,
    @JsonProperty("file_count") val fileCount: Int?,
    @JsonProperty("file_url") val fileUrl: String?,
    @JsonProperty("id") val id: String,
    @JsonProperty("params") val params: ReportResponseParams,
    @JsonProperty("status") val status: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("user_id") val userId: String?
)

data class ReportResponseParams(
    @JsonProperty("start_date") val startDate: String,
    @JsonProperty("end_date") val endDate: String,
    @JsonProperty("format") val format: String?,
    @JsonProperty("new_york_state") val newYorkState: Boolean?,
    @JsonProperty("product_id") val productId: String?,
    @JsonProperty("profile_id") val profileId: String?,
    @JsonProperty("user") val user: ReportResponseUser?
)

data class ReportResponseUser(
    @JsonProperty("active_at") val activeAt: String,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("default_profile_id") val defaultProfileId: String,
    @JsonProperty("details") val details: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("flags") val flags: String?,
    @JsonProperty("fulfills_new_requirements") val fulfillsNewRequirements: Boolean,
    @JsonProperty("has_default") val hasDefault: Boolean,
    @JsonProperty("id") val id: String,
    @JsonProperty("is_banned") val isBanned: Boolean,
    @JsonProperty("is_brokerage") val isBrokerage: Boolean,
    @JsonProperty("name") val name: String,
    @JsonProperty("oauth_client") val oauthClient: String,
    @JsonProperty("org_id") val orgId: String?,
    @JsonProperty("permissions") val permissions: String?,
    @JsonProperty("preferences") val preferences: ReportResponsePreferences,
    @JsonProperty("roles") val roles: String?,
    @JsonProperty("user_type") val userType: String
)

data class ReportResponsePreferences(
    @JsonProperty("margin_joined_waitlist") val marginJoinedWaitlist: String,
    @JsonProperty("margin_retail_promo_attempts") val marginRetailPromoAttempts: Int,
    @JsonProperty("margin_retail_web_promo_closed_in_utc") val marginRetailWebPromoClosedInUtc: String,
    @JsonProperty("mobile_app_discoverability_modal_closed_at_in_utc") val mobileAppDiscoverabilityModalClosedAtInUtc: String,
    @JsonProperty("preferred_market") val preferredMarket: String
)

class ProfilesResponse : ArrayList<ProfilesResponseItem>()

data class ProfilesResponseItem(
    @JsonProperty("active") val active: Boolean,
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("is_default") val isDefault: Boolean,
    @JsonProperty("name") val name: String,
    @JsonProperty("user_id") val userId: String
)

class UserTrailingVolumeResponse : ArrayList<TrailingVolumeResponseItem>()

data class TrailingVolumeResponseItem(
    @JsonProperty("exchange_volume") val exchangeVolume: String,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("recorded_at") val recordedAt: String,
    @JsonProperty("volume") val volume: String
)

data class MarginInformationResponse(
    @JsonProperty("available_borrow_limits") val availableBorrowLimits: MarginInformationResponseAvailableBorrowLimits,
    @JsonProperty("borrow_limit") val borrowLimit: String,
    @JsonProperty("borrow_power") val borrowPower: Double,
    @JsonProperty("buying_power") val buyingPower: Double,
    @JsonProperty("collateral_currencies") val collateralCurrencies: List<String>,
    @JsonProperty("collateral_hold_value") val collateralHoldValue: String,
    @JsonProperty("equity_percentage") val equityPercentage: Double,
    @JsonProperty("interest_paid") val interestPaid: String,
    @JsonProperty("interest_rate") val interestRate: String,
    @JsonProperty("last_liquidation_at") val lastLiquidationAt: String,
    @JsonProperty("margin_call_equity") val marginCallEquity: String,
    @JsonProperty("margin_initial_equity") val marginInitialEquity: String,
    @JsonProperty("margin_warning_equity") val marginWarningEquity: String,
    @JsonProperty("profile_id") val profileId: String,
    @JsonProperty("selling_power") val sellingPower: Double,
    @JsonProperty("top_up_amounts") val topUpAmounts: MarginInformationResponseTopUpAmounts
)

data class MarginInformationResponseAvailableBorrowLimits(
    @JsonProperty("marginable_limit") val marginableLimit: Double,
    @JsonProperty("nonmarginable_limit") val nonmarginableLimit: Double
)

data class MarginInformationResponseTopUpAmounts(
    @JsonProperty("borrowable_usd") val borrowableUsd: String,
    @JsonProperty("non_borrowable_usd") val nonBorrowableUsd: String
)

data class MarginBuyingPowerResponse(
    @JsonProperty("buying_power") val buyingPower: Double,
    @JsonProperty("buying_power_explanation") val buyingPowerExplanation: String,
    @JsonProperty("selling_power") val sellingPower: Double
)

class MarginWithdrawalPowerResponse : ArrayList<MarginWithdrawalPowerResponseItem>()

data class MarginWithdrawalPowerResponseItem(
    @JsonProperty("profile_id") val profileId: String,
    @JsonProperty("withdrawal_power") val withdrawalPower: String
)

class MarginAllWithdrawalPowersResponse : ArrayList<AllWithdrawalPowerResponseItem>()

data class AllWithdrawalPowerResponseItem(
    @JsonProperty("marginable_withdrawal_powers") val marginableWithdrawalPowers: List<MarginableWithdrawalPower>,
    @JsonProperty("profile_id") val profileId: String
)

data class MarginableWithdrawalPower(
    @JsonProperty("currency") val currency: String,
    @JsonProperty("withdrawal_power") val withdrawalPower: String
)

data class MarginExitPlanResponse(
    @JsonProperty("accountsList") val accountsList: List<MarginExitPlanResponseAccounts>,
    @JsonProperty("createdAt") val createdAt: String,
    @JsonProperty("equityPercentage") val equityPercentage: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("profileId") val profileId: String,
    @JsonProperty("strategiesList") val strategiesList: List<MarginExitPlanResponseStrategies>,
    @JsonProperty("totalAssetsUsd") val totalAssetsUsd: String,
    @JsonProperty("totalLiabilitiesUsd") val totalLiabilitiesUsd: String,
    @JsonProperty("userId") val userId: String
)

data class MarginExitPlanResponseAccounts(
    @JsonProperty("amount") val amount: String,
    @JsonProperty("currency") val currency: String,
    @JsonProperty("id") val id: String
)

data class MarginExitPlanResponseStrategies(
    @JsonProperty("accountId") val accountId: String,
    @JsonProperty("amount") val amount: String,
    @JsonProperty("orderId") val orderId: String,
    @JsonProperty("product") val product: String,
    @JsonProperty("strategy") val strategy: String,
    @JsonProperty("type") val type: String
)

class MarginLiquidationHistoryResponse : ArrayList<LiquidationHistoryResponseItem>()

data class LiquidationHistoryResponseItem(
    @JsonProperty("event_id") val eventId: String,
    @JsonProperty("event_time") val eventTime: String,
    @JsonProperty("orders") val orders: List<MarginLiquidationHistoryResponseOrder>
)

data class MarginLiquidationHistoryResponseOrder(
    @JsonProperty("created_at") val createdAt: String,
    @JsonProperty("done_at") val doneAt: String,
    @JsonProperty("done_reason") val doneReason: String,
    @JsonProperty("executed_value") val executedValue: String,
    @JsonProperty("fill_fees") val fillFees: String,
    @JsonProperty("filled_size") val filledSize: String,
    @JsonProperty("id") val id: String,
    @JsonProperty("post_only") val postOnly: Boolean,
    @JsonProperty("product_id") val productId: String,
    @JsonProperty("profile_id") val profileId: String,
    @JsonProperty("settled") val settled: Boolean,
    @JsonProperty("side") val side: String,
    @JsonProperty("size") val size: String,
    @JsonProperty("status") val status: String,
    @JsonProperty("type") val type: String
)

data class MarginPositionRefreshAmountsResponse(
    @JsonProperty("oneDayRenewalAmount") val oneDayRenewalAmount: String,
    @JsonProperty("twoDayRenewalAmount") val twoDayRenewalAmount: String
)

data class MarginStatusResponse(
    @JsonProperty("eligible") val eligible: Boolean,
    @JsonProperty("enabled") val enabled: Boolean,
    @JsonProperty("tier") val tier: Int
)

data class OracleResponse(
    @JsonProperty("messages") val messages: List<String>,
    @JsonProperty("prices") val prices: OracleResponsePrices,
    @JsonProperty("signatures") val signatures: List<String>,
    @JsonProperty("timestamp") val timestamp: String
)

data class OracleResponsePrices(
    @JsonProperty("BTC") val btc: String,
    @JsonProperty("ETH") val eth: String,
    @JsonProperty("XTZ") val xtz: String,
    @JsonProperty("DAI") val dai: String,
    @JsonProperty("REP") val rep: String,
    @JsonProperty("ZRX") val zrx: String,
    @JsonProperty("BAT") val bat: String,
    @JsonProperty("KNC") val knc: String,
    @JsonProperty("LINK") val link: String,
    @JsonProperty("COMP") val comp: String,
    @JsonProperty("UNI") val uni: String
)

