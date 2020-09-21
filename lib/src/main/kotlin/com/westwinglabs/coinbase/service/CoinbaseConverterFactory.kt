package com.westwinglabs.coinbase.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import retrofit2.Converter
import retrofit2.converter.jackson.JacksonConverterFactory

internal class CoinbaseConverterFactory {

    companion object {
        val mapper: ObjectMapper = ObjectMapper()
            .registerModule(KotlinModule())

        fun create(): Converter.Factory {
            return JacksonConverterFactory.create(mapper)
        }
    }
}
