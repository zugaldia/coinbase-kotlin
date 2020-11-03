let shared = require('./shared');

console.log("--- empty body --")
let timestamp1 = Date.now() / 1000;
let signature1 = shared.sign(timestamp1, 'GET', '/accounts', '')
console.log(timestamp1)
console.log(signature1)

console.log("--- non-empty body --")
let timestamp2 = Date.now() / 1000;
let encodedBody = JSON.stringify({"size": "0.01", "price": "0.100", "side": "buy", "product_id": "BTC-USD"});
let signature2 = shared.sign(timestamp2, 'POST', '/orders', encodedBody)
console.log(timestamp2)
console.log(signature2)
