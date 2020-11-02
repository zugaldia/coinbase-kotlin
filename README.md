![build](https://github.com/westwinglabs/coinbase-kotlin/workflows/build/badge.svg)
[ ![Download](https://api.bintray.com/packages/westwinglabs/coinbase/coinbase-kotlin/images/download.svg) ](https://bintray.com/westwinglabs/coinbase/coinbase-kotlin/_latestVersion)

# Coinbase (Kotlin/Java)

This library provides a Kotlin client, which is Java-compatible, for [Coinbase Pro](https://docs.pro.coinbase.com).
It targets Java 8 and includes minimal dependencies so that it can be used across server-side, command-line, or desktop projects, including Android.

## Installation

1. Set up the Bintray repo:

```
repositories {
    maven {
        url  "https://dl.bintray.com/westwinglabs/coinbase" 
    }
}
```

2. Add the dependency to your project:

```
implementation 'com.westwinglabs:coinbase-kotlin:0.2.0'
```

For Maven instructions, [visit the project page](https://bintray.com/westwinglabs/coinbase/coinbase-kotlin) on Bintray.

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

For additional documentation visit [`USAGE.md`](USAGE.md).

## Issues

If you encounter any problems while using this library, [open a ticket](https://github.com/westwinglabs/coinbase-kotlin/issues) in this repository.
We welcome the participation and contributions from everyone.

## License

This library is Open Source, distributed under the terms of the [Apache License, Version 2.0](LICENSE).

This project is not formally affiliated with Coinbase.
