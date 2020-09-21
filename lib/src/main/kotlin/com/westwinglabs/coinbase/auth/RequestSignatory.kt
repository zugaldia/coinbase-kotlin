package com.westwinglabs.coinbase.auth

import org.apache.commons.codec.binary.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class RequestSignatory(private val apiSecret: String) {

    companion object {
        // https://docs.oracle.com/javase/8/docs/api/javax/crypto/Mac.html
        const val ALGORITHM = "HmacSHA256"
    }

    fun sign(method: String, path: String, body: String): Pair<String, String> {
        // The CB-ACCESS-TIMESTAMP header MUST be number of seconds since
        // Unix Epoch in UTC. Decimal values are allowed.
        val timestamp = (System.currentTimeMillis() / 1_000).toString()

        // Decode secret and init the MAC algorithm
        val decoded = Base64.decodeBase64(apiSecret)
        val keySpec = SecretKeySpec(decoded, ALGORITHM)
        val mac = Mac.getInstance(ALGORITHM)
        mac.init(keySpec)

        // Create the signature for this request
        val what = (timestamp + method + path + body).toByteArray()
        val result = mac.doFinal(what)
        val signature = Base64.encodeBase64String(result)

        return Pair(timestamp, signature)
    }
}
