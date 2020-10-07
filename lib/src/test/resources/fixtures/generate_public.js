/*
 * Generate fixtures for the public API
 * https://docs.pro.coinbase.com/#market-data
 */

let request = require('request');
let fs = require('fs');

let ENDPOINT = "https://api.pro.coinbase.com";

let headers = {'User-Agent': 'FixtureGenerator'}

function write_response(err, res, body, path) {
    if (err) {
        console.log("Error: " + err);
    } else {
        console.log("Writing: " + path);
        fs.writeFileSync("public/" + path, JSON.stringify(body, null, 2));
    }
}

request({
    method: 'GET',
    url: `${ENDPOINT}/products`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_products.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/BTC-USD`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_product.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/XXX-YYY`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_error.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/BTC-USD/book`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_product_book.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/BTC-USD/ticker`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_product_ticker.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/BTC-USD/trades`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_product_trades.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/BTC-USD/candles`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_product_candles.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/products/BTC-USD/stats`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_product_stats.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/currencies`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_currencies.json"));

request({
    method: 'GET',
    url: `${ENDPOINT}/time`,
    headers: headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_time.json"));
