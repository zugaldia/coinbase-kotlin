package com.westwinglabs.coinbase.service

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import retrofit2.Converter
import retrofit2.converter.jackson.JacksonConverterFactory

internal class CoinbaseConverterFactory {

    companion object {
        val mapper: ObjectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            // Only properties with non-null values are to be included
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            // Encountering unknown properties should not result in a failure
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        fun create(): Converter.Factory {
            return JacksonConverterFactory.create(mapper)
        }
    }
}
