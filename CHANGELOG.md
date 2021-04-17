# Changelog

All notable changes to this project will be documented in this file.
We welcome the participation and contributions from everyone.

## [0.4.0] - 2021-04-16

Note: Starting with this version, artifacts are now available on GitHub Packages. Bintray is no longer supported.

- Add functionality for authentication of subscription requests [#21](https://github.com/westwinglabs/coinbase-kotlin/pull/21)
- Add post parameters for orders [#23](https://github.com/westwinglabs/coinbase-kotlin/pull/23)
- Ticker `sequence` and `trade_id` fields should be long [#16](https://github.com/westwinglabs/coinbase-kotlin/pull/16) [#17](https://github.com/westwinglabs/coinbase-kotlin/pull/17)
- Bump stable dependencies [#18](https://github.com/westwinglabs/coinbase-kotlin/pull/18) [#18](https://github.com/westwinglabs/coinbase-kotlin/pull/22)

## [0.3.0] - 2020-11-19

- Update to Kotlin 1.4 [#6](https://github.com/westwinglabs/coinbase-kotlin/pull/6)
- Add more data classes and support to all channels in websocket feed [#14](https://github.com/westwinglabs/coinbase-kotlin/pull/14)
- Fix signature for paths with query parameters [#13](https://github.com/westwinglabs/coinbase-kotlin/pull/13)

## [0.2.0] - 2020-11-02

- Introduce the header interceptor at the beginning of the HTTP request chain [#10](https://github.com/westwinglabs/coinbase-kotlin/pull/10) [#11](https://github.com/westwinglabs/coinbase-kotlin/pull/11)
- Add option to the sample CLI to specify endpoint type [#9](https://github.com/westwinglabs/coinbase-kotlin/pull/9)
- Replace SLF4J with Log4j2 [#5](https://github.com/westwinglabs/coinbase-kotlin/pull/5) [#7](https://github.com/westwinglabs/coinbase-kotlin/pull/7) [#8](https://github.com/westwinglabs/coinbase-kotlin/pull/8)
- Remove unnecessary Apache Commons (`commons-lang3`) dependency [#4](https://github.com/westwinglabs/coinbase-kotlin/pull/4)

## [0.1.0] - 2020-10-10

- Initial release with support for public, private, and websocket APIs.
