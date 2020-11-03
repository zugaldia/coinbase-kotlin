/*
 * Generate fixtures for the public API
 * https://docs.pro.coinbase.com/#private
 */

let request = require('request');
let fs = require('fs');
let uuid = require('uuid');
let shared = require('./shared');

// https://public.sandbox.pro.coinbase.com/
let ENDPOINT = "https://api-public.sandbox.pro.coinbase.com";

let defaultProductId = "BTC-USD";

let common_headers = {
    'User-Agent': 'FixtureGenerator',
    'CB-ACCESS-KEY': process.env.COINBASE_API_KEY,
    'CB-ACCESS-PASSPHRASE': process.env.COINBASE_API_PASSPHRASE
}

function get_headers(method, requestPath, body) {
    let timestamp = Date.now() / 1000;
    let encodedBody = body === '' ? body : JSON.stringify(body);
    return Object.assign({}, common_headers, {
        'CB-ACCESS-TIMESTAMP': timestamp,
        'CB-ACCESS-SIGN': shared.sign(timestamp, method, requestPath, encodedBody)
    });
}

function write_response(err, res, body, path) {
    if (err) {
        console.log("Error: " + err);
    } else {
        console.log("Writing: " + path);
        fs.writeFileSync("private/" + path, JSON.stringify(body, null, 2));
    }
}

function slow_request(options, callback) {
    // Randomly space requests over half a minute to avoid "Private rate limit exceeded" errors
    setTimeout(function () {
        request(options, callback)
        // Timestamp must be within 30 seconds of the api service time
    }, Math.random() * 1000 * 25);
}

/*
 * Accounts
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/accounts`,
    headers: get_headers('GET', '/accounts', ''),
    json: true
}, (err, res, body) => {
    write_response(err, res, body, "get_accounts.json");
    let accountId = body[0]['id'];

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/accounts/${accountId}`,
        headers: get_headers('GET', `/accounts/${accountId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_account.json'));

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/accounts/${accountId}/ledger`,
        headers: get_headers('GET', `/accounts/${accountId}/ledger`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_account_ledger.json'));

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/accounts/${accountId}/holds`,
        headers: get_headers('GET', `/accounts/${accountId}/holds`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_account_holds.json'));
});

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/accounts`,
    headers: common_headers,
    json: true
}, (err, res, body) => write_response(err, res, body, "get_error.json"));

/*
 * Orders
 */

let orderUuid = uuid.v4();
let orderRequest = {
    "size": "0.01",
    "price": "0.100",
    "side": "buy",
    "product_id": defaultProductId,
    "client_oid": orderUuid
};

slow_request({
    method: 'POST',
    url: `${ENDPOINT}/orders`,
    headers: get_headers('POST', '/orders', orderRequest),
    body: orderRequest,
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'post_orders.json')

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/orders`,
        headers: get_headers('GET', '/orders', ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_orders.json'));

    let orderId = body['id'];

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/orders/${orderId}`,
        headers: get_headers('GET', `/orders/${orderId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_order.json'));

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/orders/client:${orderUuid}`,
        headers: get_headers('GET', `/orders/client:${orderUuid}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_order_by_client.json'));

    // Responses are similar for the following two, but it'd be good to add fixtures regardless
    // TODO: We can also delete orders by client ID (DELETE /orders/client:<client_oid>)
    // TODO: We can also delete orders in bulk (DELETE /orders)
    slow_request({
        method: 'DELETE',
        url: `${ENDPOINT}/orders/${orderId}`,
        headers: get_headers('DELETE', `/orders/${orderId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'delete_order.json'));
});

/*
 * Fills
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/fills?product_id=${defaultProductId}`,
    headers: get_headers('GET', `/fills?product_id=${defaultProductId}`, ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_fills.json'));

/*
 * Limits
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/users/self/exchange-limits`,
    headers: get_headers('GET', `/users/self/exchange-limits`, ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_user_exchange_limits.json'));

/*
 * Payment Methods
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/payment-methods`,
    headers: get_headers('GET', '/payment-methods', ''),
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'get_payment_methods.json')
    let usdPaymentMethodId = body.filter(paymentMethod => paymentMethod['currency'] === 'USD')[0]['id'];
    doDepositFromPaymentMethod(usdPaymentMethodId);
    doWithdrawToPaymentMethod(usdPaymentMethodId);
});

/*
 * Coinbase Accounts
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/coinbase-accounts`,
    headers: get_headers('GET', '/coinbase-accounts', ''),
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'get_coinbase_accounts.json');
    let btcCoinbaseAccountId = body.filter(paymentMethod => paymentMethod['currency'] === 'BTC')[0]['id'];
    doDepositFromCoinbase(btcCoinbaseAccountId);
    doWithdrawToCoinbase(btcCoinbaseAccountId);
});

/*
 * Deposits
 */

function doDepositFromPaymentMethod(usdPaymentMethodId) {
    let depositRequest = {
        "amount": 1.00,
        "currency": "USD",
        "payment_method_id": usdPaymentMethodId
    };

    slow_request({
        method: 'POST',
        url: `${ENDPOINT}/deposits/payment-method`,
        headers: get_headers('POST', '/deposits/payment-method', depositRequest),
        body: depositRequest,
        json: true
    }, (err, res, body) => write_response(err, res, body, 'post_deposit_from_payment.json'));
}

function doDepositFromCoinbase(btcCoinbaseAccountId) {
    let depositRequest = {
        "amount": 0.1,
        "currency": "BTC",
        "coinbase_account_id": btcCoinbaseAccountId,
    };

    slow_request({
        method: 'POST',
        url: `${ENDPOINT}/deposits/coinbase-account`,
        headers: get_headers('POST', '/deposits/coinbase-account', depositRequest),
        body: depositRequest,
        json: true
    }, (err, res, body) => write_response(err, res, body, 'post_deposit_from_coinbase.json'));

    slow_request({
        method: 'POST',
        url: `${ENDPOINT}/coinbase-accounts/${btcCoinbaseAccountId}/addresses`,
        headers: get_headers('POST', `/coinbase-accounts/${btcCoinbaseAccountId}/addresses`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'post_deposit_from_coinbase_address.json'));
}

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/transfers?type=deposit`,
    headers: get_headers('GET', '/transfers?type=deposit', ''),
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'get_deposits.json')

    let transferId = body[0]['id'];
    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/transfers/${transferId}`,
        headers: get_headers('GET', `/transfers/${transferId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_deposit.json'));

});

/*
 * Withdrawals
 */

function doWithdrawToPaymentMethod(usdPaymentMethodId) {
    let withdrawRequest = {
        "amount": 1.00,
        "currency": "USD",
        "payment_method_id": usdPaymentMethodId
    };

    slow_request({
        method: 'POST',
        url: `${ENDPOINT}/withdrawals/payment-method`,
        headers: get_headers('POST', `/withdrawals/payment-method`, withdrawRequest),
        body: withdrawRequest,
        json: true
    }, (err, res, body) => write_response(err, res, body, 'post_withdraw_to_payment.json'));
}

function doWithdrawToCoinbase(btcCoinbaseAccountId) {
    let withdrawRequest = {
        "amount": 0.1,
        "currency": "BTC",
        "coinbase_account_id": btcCoinbaseAccountId,
    };

    slow_request({
        method: 'POST',
        url: `${ENDPOINT}/withdrawals/coinbase-account`,
        headers: get_headers('POST', '/withdrawals/coinbase-account', withdrawRequest),
        body: withdrawRequest,
        json: true
    }, (err, res, body) => write_response(err, res, body, 'post_withdraw_to_coinbase.json'));
}

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/transfers?type=withdraw`,
    headers: get_headers('GET', '/transfers?type=withdraw', ''),
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'get_withdraws.json')

    let transferId = body[0]['id'];
    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/transfers/${transferId}`,
        headers: get_headers('GET', `/transfers/${transferId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_withdraw.json'));
});

let withdrawToCryptoRequest = {
    "amount": 0.1,
    "currency": "BTC",
    "crypto_address": "0xmswUGcPHp1YnkLCgF1TtoryqSc5E9Q8xFa"
};

slow_request({
    method: 'POST',
    url: `${ENDPOINT}/withdrawals/crypto`,
    headers: get_headers('POST', '/withdrawals/crypto', withdrawToCryptoRequest),
    body: withdrawToCryptoRequest,
    json: true
}, (err, res, body) => write_response(err, res, body, 'post_withdraw_to_crypto.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/withdrawals/fee-estimate?currency=BTC&crypto_address=0xmswUGcPHp1YnkLCgF1TtoryqSc5E9Q8xFa`,
    headers: get_headers('GET', '/withdrawals/fee-estimate?currency=BTC&crypto_address=0xmswUGcPHp1YnkLCgF1TtoryqSc5E9Q8xFa', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_withdrawals_fee_estimate.json'));

/*
 * Stablecoin Conversions
 */

let conversionRequest = {
    "from": "USD",
    "to": "USDC",
    "amount": "100.00"
}

slow_request({
    method: 'POST',
    url: `${ENDPOINT}/conversions`,
    headers: get_headers('POST', '/conversions', conversionRequest),
    json: true,
    body: conversionRequest
}, (err, res, body) => write_response(err, res, body, 'post_conversions.json'));

/*
 * Fees
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/fees`,
    headers: get_headers('GET', '/fees', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_fees.json'));

/*
 * Reports
 */

let lastMonth = new Date()
lastMonth.setMonth(lastMonth.getMonth() - 1);
let reportRequestFills = {
    "type": "fills",
    "product_id": defaultProductId,
    "start_date": lastMonth.toISOString(),
    "end_date": new Date().toISOString()
}

slow_request({
    method: 'POST',
    url: `${ENDPOINT}/reports`,
    headers: get_headers('POST', '/reports', reportRequestFills),
    body: reportRequestFills,
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'post_reports_fills.json');
    let reportId = body['id'];
    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/reports/${reportId}`,
        headers: get_headers('GET', `/reports/${reportId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_report_fills.json'));
});

/*
 * Profiles
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/profiles`,
    headers: get_headers('GET', '/profiles', ''),
    json: true
}, (err, res, body) => {
    write_response(err, res, body, 'get_profiles.json')

    let profileFromId = body[0]['id'];
    let profileToId = body[1]['id'];

    slow_request({
        method: 'GET',
        url: `${ENDPOINT}/profiles/${profileFromId}`,
        headers: get_headers('GET', `/profiles/${profileFromId}`, ''),
        json: true
    }, (err, res, body) => write_response(err, res, body, 'get_profile.json'));

    let transferRequest = {
        "from": profileFromId,
        "to": profileToId,
        "currency": "BTC",
        "amount": "0.01"
    };

    slow_request({
        method: 'POST',
        url: `${ENDPOINT}/profiles/transfer`,
        headers: get_headers('POST', `/profiles/transfer`, transferRequest),
        body: transferRequest,
        json: true
    }, (err, res, body) => write_response(err, res, body, 'post_profile_transfer.json'));
});

/*
 * User Account
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/users/self/trailing-volume`,
    headers: get_headers('GET', '/users/self/trailing-volume', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_user_trailing_volume.json'));

/*
 * Margin
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/profile_information?product_id=${defaultProductId}`,
    headers: get_headers('GET', `/margin/profile_information?product_id=${defaultProductId}`, ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_profile_information.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/buying_power?product_id=${defaultProductId}`,
    headers: get_headers('GET', `/margin/buying_power?product_id=${defaultProductId}`, ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_buying_power.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/withdrawal_power?currency=BTC`,
    headers: get_headers('GET', `/margin/withdrawal_power?currency=BTC`, ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_withdrawal_power.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/withdrawal_power_all`,
    headers: get_headers('GET', '/margin/withdrawal_power_all', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_withdrawal_power_all.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/exit_plan`,
    headers: get_headers('GET', '/margin/exit_plan', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_exit_plan.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/liquidation_history`,
    headers: get_headers('GET', '/margin/liquidation_history', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_liquidation_history.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/position_refresh_amounts`,
    headers: get_headers('GET', '/margin/position_refresh_amounts', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_position_refresh_amounts.json'));

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/margin/status`,
    headers: get_headers('GET', '/margin/status', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_margin_status.json'));

/*
 * Oracle
 */

slow_request({
    method: 'GET',
    url: `${ENDPOINT}/oracle`,
    headers: get_headers('GET', '/oracle', ''),
    json: true
}, (err, res, body) => write_response(err, res, body, 'get_oracle.json'));
