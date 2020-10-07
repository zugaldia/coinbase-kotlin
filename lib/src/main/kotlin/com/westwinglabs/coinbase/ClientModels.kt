package com.westwinglabs.coinbase

import org.slf4j.LoggerFactory

abstract class CoinbaseCallback<T> {

    private val logger = LoggerFactory.getLogger("CoinbaseCallback")

    abstract fun onResponse(result: T?)

    // Optional to override
    open fun onFailure(code: Int, message: String, throwable: Throwable) {
        logger.error("Error ($code): $message", throwable)
    }
}

class CoinbaseException(override val message: String) : RuntimeException(message)
