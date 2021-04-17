package com.westwinglabs.coinbase

import org.junit.Assert
import org.junit.Test

class CoinbaseClientPrivateTest : BaseMockedServer() {

    @Test
    fun testGetAccountsMocked() {
        val response = mockedClient.getAccounts()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetAccountMocked() {
        val response = mockedClient.getAccount(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetAccountLedgerMocked() {
        val response = mockedClient.getAccountLedger(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetAccountHoldsMocked() {
        val response = mockedClient.getAccountHolds(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testPostOrderMocked() {
        val response = mockedClient.postOrder(
            side = "buy",
            productId = PRODUCT_VALID,
            price = "0.100",
            size = "0.01"
        )
        Assert.assertNotNull(response)
    }

    @Test
    fun testCancelOrderMocked() {
        val response = mockedClient.cancelOrder(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testCancelOrdersByClientIdMocked() {
        val response = mockedClient.cancelOrdersByClientId(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testCancelOrdersMocked() {
        val response = mockedClient.cancelOrders()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetOrdersMocked() {
        val response = mockedClient.getOrders()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetOrderMocked() {
        val response = mockedClient.getOrder(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetOrderByClientIdMocked() {
        val response = mockedClient.getOrderByClientId(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetFillsMocked() {
        val response = mockedClient.getFills()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetExchangeLimitsMocked() {
        val response = mockedClient.getExchangeLimits()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetDepositsMocked() {
        val response = mockedClient.getDeposits()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetTransferMocked() {
        val response = mockedClient.getTransfer(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testDepositFromPaymentMethodMocked() {
        val response = mockedClient.depositFromPaymentMethod("10.0", "USD", SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testDepositFromCoinbaseMocked() {
        val response = mockedClient.depositFromCoinbase("10.0", "BTC", SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGenerateAddressForDepositMocked() {
        val response = mockedClient.generateAddressForDeposit(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

//    @Test
//    fun testGetWithdrawalsMocked() {
//        val response = mockedClient.getWithdrawals()
//        Assert.assertNotNull(response)
//    }

    @Test
    fun testWithdrawToPaymentMethodMocked() {
        val response = mockedClient.withdrawToPaymentMethod("10.0", "USD", SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testWithdrawToCoinbaseMocked() {
        val response = mockedClient.withdrawToCoinbase("10.0", "BTC", SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testWithdrawToCryptoMocked() {
        val response = mockedClient.withdrawToCrypto("10.0", "BTC", SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetFeeEstimateForAddressMocked() {
        val response = mockedClient.getFeeEstimateForAddress("BTC", SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testConvertMocked() {
        val response = mockedClient.convert("USD", "USDC", "10.0")
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetPaymentMethodsMocked() {
        val response = mockedClient.getPaymentMethods()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetCoinbaseAccountsMocked() {
        val response = mockedClient.getCoinbaseAccounts()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetFeesMocked() {
        val response = mockedClient.getFees()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGenerateReportMocked() {
        val response = mockedClient.generateReport(startDate = "xxx", endDate = "yyy")
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetReportMocked() {
        val response = mockedClient.getReport(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProfilesMocked() {
        val response = mockedClient.getProfiles()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetProfileMocked() {
        val response = mockedClient.getProfile(SAMPLE_UUID)
        Assert.assertNotNull(response)
    }

    @Test
    fun testTransferBetweenProfilesMocked() {
        val response = mockedClient.transferBetweenProfiles(SAMPLE_UUID, SAMPLE_UUID, "BTC", "10.0")
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetUserTrailingVolumeMocked() {
        val response = mockedClient.getUserTrailingVolume()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginInformationMocked() {
        val response = mockedClient.getMarginInformation("BTC-USD")
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginBuyingPowerMocked() {
        val response = mockedClient.getMarginBuyingPower("BTC-USD")
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginWithdrawalPowerMocked() {
        val response = mockedClient.getMarginWithdrawalPower("BTC")
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginAllWithdrawalPowersMocked() {
        val response = mockedClient.getMarginAllWithdrawalPowers()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginExitPlanMocked() {
        val response = mockedClient.getMarginExitPlan()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginLiquidationHistoryMocked() {
        val response = mockedClient.getMarginLiquidationHistory()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginPositionRefreshAmountsMocked() {
        val response = mockedClient.getMarginPositionRefreshAmounts()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetMarginStatusMocked() {
        val response = mockedClient.getMarginStatus()
        Assert.assertNotNull(response)
    }

    @Test
    fun testGetOracleMocked() {
        val response = mockedClient.getOracle()
        Assert.assertNotNull(response)
    }
}
