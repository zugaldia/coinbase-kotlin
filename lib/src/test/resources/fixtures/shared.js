let crypto = require('crypto');

let secret = process.env.COINBASE_API_SECRET;

module.exports = {
    sign: function (timestamp, method, requestPath, body) {
        let what = timestamp + method + requestPath + body;
        let key = Buffer(secret, 'base64');
        let hmac = crypto.createHmac('sha256', key);
        return hmac.update(what).digest('base64');
    }
};
