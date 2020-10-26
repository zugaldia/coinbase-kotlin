package com.westwinglabs.coinbase

import com.westwinglabs.coinbase.service.CoinbaseService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal fun <T> retrofit2.Call<T>.enqueueWith(callback: CoinbaseCallback<T>) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                callback.onResponse(response.body())
            } else {
                val message = response.message()
                callback.onFailure(response.code(), message, CoinbaseException(message))
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            callback.onFailure(0, throwable.message ?: "", throwable)
        }
    })
}

internal fun CoinbaseService.validatePrivate(apiKey: String, apiPassphrase: String): CoinbaseService {
    check(apiKey.isNotBlank()) { "API key is required to invoke this method." }
    check(apiPassphrase.isNotBlank()) { "API passphrase is required to invoke this method." }
    return this
}
