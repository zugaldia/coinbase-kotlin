package com.westwinglabs.coinbase.cli

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options

const val OPTION_HELP = "help"

const val OPTION_API_ENDPOINT = "endpoint"
const val OPTION_API_KEY = "apikey"
const val OPTION_API_SECRET = "secret"
const val OPTION_API_PASSPHRASE = "passphrase"

const val OPTION_SAMPLE_SIGNATURE = "sample_signature"
const val OPTION_SIGNATURE_METHOD = "method"
const val OPTION_SIGNATURE_PATH = "path"
const val OPTION_SIGNATURE_BODY = "body"

const val OPTION_SAMPLE_PRIVATE = "sample_private"
const val OPTION_SAMPLE_PUBLIC = "sample_public"
const val OPTION_SAMPLE_WEBSOCKET = "sample_websocket"

fun main(args: Array<String>) {
    val options = Options()
    options.addOption(OPTION_HELP, false, "Print usage information")
    options.addOption(OPTION_API_ENDPOINT, true, "Coinbase endpoint ('production' or 'sandbox')")
    options.addOption(OPTION_API_KEY, true, "Coinbase API key")
    options.addOption(OPTION_API_SECRET, true, "Coinbase API secret")
    options.addOption(OPTION_API_PASSPHRASE, true, "Coinbase API passphrase")
    options.addOption(OPTION_SAMPLE_SIGNATURE, false, "Print the signature for an authenticated request")
    options.addOption(OPTION_SIGNATURE_METHOD, true, "The method for the request (required for signing)")
    options.addOption(OPTION_SIGNATURE_PATH, true, "The path for the request (required for signing)")
    options.addOption(OPTION_SIGNATURE_BODY, true, "The JSON body of the request (optional for signing)")
    options.addOption(OPTION_SAMPLE_PRIVATE, false, "Sample private request (requires authentication)")
    options.addOption(OPTION_SAMPLE_PUBLIC, false, "Sample public request (does not require authentication)")
    options.addOption(OPTION_SAMPLE_WEBSOCKET, false, "Sample websocket connection")

    val parser = DefaultParser()
    val parsed = parser.parse(options, args)
    when {
        parsed.hasOption(OPTION_SAMPLE_SIGNATURE) -> CoinbaseCli().sampleSignature(parsed)
        parsed.hasOption(OPTION_SAMPLE_PRIVATE) -> CoinbaseCli().samplePrivate(parsed)
        parsed.hasOption(OPTION_SAMPLE_PUBLIC) -> CoinbaseCli().samplePublic(parsed)
        parsed.hasOption(OPTION_SAMPLE_WEBSOCKET) -> CoinbaseCli().sampleWebsocket(parsed)
        else -> {
            val formatter = HelpFormatter()
            formatter.printHelp("java -jar cli.jar", options, true)
        }
    }
}
