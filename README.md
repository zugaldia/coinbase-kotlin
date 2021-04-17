![build](https://github.com/westwinglabs/coinbase-kotlin/workflows/build/badge.svg)

# Coinbase (Kotlin/Java)

This library provides a Kotlin client, which is Java-compatible, for [Coinbase Pro](https://docs.pro.coinbase.com).
It targets Java 8 and includes minimal dependencies so that it can be used across server-side, command-line, or desktop projects, including Android.

## Installation

1. Set up GitHub Packages:

```
repositories {
    maven {
        url "https://maven.pkg.github.com/westwinglabs/coinbase-kotlin" 
    }
}
```

2. Add the dependency to your project:

```
implementation 'com.westwinglabs:coinbase-kotlin:0.4.0'
```

For more information on how to set up Gradle for use with GitHub Packages [visit this guide](https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages).

## Usage

The following code snippets are extracted from the [sample CLI app](cli) included with this project.

### Public API

Use the `CoinbaseClient` to query the public API:

```
val client = CoinbaseClient()
val response = client.getProducts()
println(response.first().id) // ETH-BTC
```

### Private API

To query the private API endpoint, you'll need to pass your Coinbase credentials to the `CoinbaseClient` client:

```
val client = CoinbaseClient(apiKey = "xxx", apiSecret = "yyy", apiPassphrase = "zzz")
println(client.getAccounts())
```

All methods, from the public and the private API, are available as synchronous calls (above), or asynchronous via callbacks:

```
client.getAccounts(object : CoinbaseCallback<AccountsResponse>() {
    override fun onResponse(result: AccountsResponse?) {
        println(result)
    }
})
```

Asynchronous callbacks accept an optional `onError` method with any error information.

### Websocket feed

To access real-time market data updates for orders and trades you'll want to use the websocket interface.
This is how you'd open a connection, subscribe to heartbeat updates, and process incoming messages:

```
val client = CoinbaseClient()
client.openFeed(this) // The class implements the FeedListener interface
...

override fun onOpen() {
    val request = SubscribeRequest(channels = listOf("heartbeat"), productIds = listOf("ETH-BTC"))
    client.subscribe(request) // Subscribe to heartbeat messages
}

override fun onHeartbeatMessage(message: HeartbeatResponse) = println(message) // Process message
```

You can also authenticate a subscription request to receive additional attribution in channel messaging.
From the Docs:
    Authentication will result in a couple of benefits:
    1. Messages where you're one of the parties are expanded and have more useful fields
    1. You will receive private messages, such as lifecycle information about stop orders you placed

This is how you would authenticate a subscription request:

    override fun onOpen() {
        secret = "secret"
        key = "key"
        passphrase = "passphrase"

        // Defaults for Websocket authentication
        method = "GET"
        path = "/users/self/verify"
        body = ""

        val signatory = RequestSignatory(secret)
        val (timestamp, signature) = signatory.sign(
            method = method,
            path = path,
            body = body
        )

        val authenticatedRequest = SubscribeRequestAuthenticated(
            signature = signature,
            key = key,
            passphrase = passphrase,
            timestamp = timestamp,
            channels = listOf(CoinbaseClient.CHANNEL_HEARTBEAT, CoinbaseClient.CHANNEL_LEVEL2),
            productIds = listOf("ETH-BTC", "BTC-USD")
        )
        
       client.subscribeAuthenticated(authenticatedRequest) // Subscribe to heartbeat messages
    }

For additional documentation visit [`USAGE.md`](USAGE.md).

## Issues

If you encounter any problems while using this library, [open a ticket](https://github.com/westwinglabs/coinbase-kotlin/issues) in this repository.
We welcome the participation and contributions from everyone.

## License

This library is Open Source, distributed under the terms of the [Apache License, Version 2.0](LICENSE).

This project is not formally affiliated with Coinbase.
