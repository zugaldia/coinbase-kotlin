package com.westwinglabs.coinbase.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CoinbaseService {

    companion object {
        const val HEADER_KEY = "CB-ACCESS-KEY"
        const val HEADER_PASSPHRASE = "CB-ACCESS-PASSPHRASE"

        // The values for these headers are added in the interceptor
        const val HEADER_TIMESTAMP = "CB-ACCESS-TIMESTAMP"
        const val HEADER_SIGN = "CB-ACCESS-SIGN"

        // Private endpoints
        const val ENDPOINT_PRIVATE_ACCOUNTS = "/accounts"

        // Public endpoints
        const val ENDPOINT_PUBLIC_PRODUCTS = "/products"
    }

    /*
     * Private API
     */

    @GET(ENDPOINT_PRIVATE_ACCOUNTS)
    fun getAccounts(
        @Header(HEADER_KEY) accessKey: String,
        @Header(HEADER_PASSPHRASE) accessPassphrase: String
    ): Call<AccountsResponse>

    /*
     * Public API
     */

    @GET("/products")
    fun getProducts(): Call<ProductsResponse>

}
