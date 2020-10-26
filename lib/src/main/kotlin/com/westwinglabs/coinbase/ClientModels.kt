package com.westwinglabs.coinbase

import org.apache.logging.log4j.LogManager

abstract class CoinbaseCallback<T> {

    private val logger = LogManager.getLogger()

    abstract fun onResponse(result: T?)

    // Optional to override
    open fun onFailure(code: Int, message: String, throwable: Throwable) {
        logger.error("Error ($code): $message", throwable)
    }
}

class CoinbaseException(override val message: String) : RuntimeException(message)
