package com.westwinglabs.coinbase

import com.westwinglabs.coinbase.service.BaseModel
import com.westwinglabs.coinbase.service.CoinbaseService
import org.apache.commons.lang3.Validate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T : BaseModel> retrofit2.Call<T>.enqueueWith(callback: CoinbaseCallback<T>) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.code() == 200) {
                callback.onResponse(response.body())
            } else {
                val message = response.body()?.message ?: ""
                callback.onFailure(response.code(), message, CoinbaseException(message))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onFailure(0, t.message ?: "", t)
        }
    })
}

fun CoinbaseService.validatePrivate(apiKey: String, apiPassphrase: String): CoinbaseService {
    Validate.notBlank(apiKey, "API key is required to invoke this method.")
    Validate.notBlank(apiPassphrase, "API passphrase is required to invoke this method.")
    return this
}
