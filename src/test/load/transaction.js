import http from 'k6/http';

export const options = {

    vus: 10, //virtual users to use at start
    stages: [
        {duration: '10s', target: 10},
        {duration: '15s', target: 50},
        {duration: '20s', target: 1000},
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
    const ENDPOINT = __ENV.ENDPOINT || `api/${API_VERSION}/transactions/transfer`;
    const url = `${HOSTNAME}:${PORT}/${ENDPOINT}`;


    const params = {
        headers: {
            'Content-Type': 'application/json',
        }
    }

    const payload = JSON.stringify(__ENV.PAYLOAD || {
        "sourceAccountId": 2,
        "targetAccountId": 1,
        "amount": 1,
        "currency": "EUR"
    })

    http.post(url, payload, params);
}
