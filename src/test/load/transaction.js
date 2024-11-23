import http from 'k6/http';

export const options = {

    vus: 10, //virtual users to use at start
    stages: [
        {duration: '5s', target: 10},
        {duration: '5s', target: 50},
        {duration: '5s', target: 1000},
    ],
    cloud: {
        projectID: "3698047",
        name: "transaction.js"
    }
};

export default function () {
    const HOSTNAME = __ENV.HOSTNAME || 'http://localhost';
    const PORT = __ENV.PORT || 8080;
    const API_VERSION = __ENV.API_VERSION || 'v1';
    const ENDPOINT = __ENV.ENDPOINT || `api/${API_VERSION}/transactions/initiate`;
    const url = `${HOSTNAME}:${PORT}/${ENDPOINT}`;


    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Cookie': __ENV.SESSION_COOKIE
        }
    }

    const payload = JSON.stringify(__ENV.PAYLOAD || {
        "sourceAccountId": 12501,
        "targetAccountId": 7507,
        "amount": 1,
        "currency": "EUR",
        "transactionType": "CARD"
    })

    http.post(url, payload, params);
}
