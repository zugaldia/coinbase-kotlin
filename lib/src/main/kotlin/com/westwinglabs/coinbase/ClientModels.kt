package com.westwinglabs.coinbase

interface CoinbaseCallback<T> {
    fun onResponse(result: T?)
    fun onFailure(code: Int, message: String, throwable: Throwable)
}

class CoinbaseException(override val message: String) : RuntimeException(message)
