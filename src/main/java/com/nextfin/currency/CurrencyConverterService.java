package com.nextfin.currency;

import com.nextfin.currency.dto.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyConverterService {

    @Value("${com.nextfin-executor.currency-converter.root-uri}")
    private String rootURI;

    @Value("${com.nextfin-executor.currency-converter.api-key}")
    private String apiKey;


    private final RestTemplate restTemplate;

    /**
     * Converts the specified amount in the specified currency to the equivalent amount in the source and target currency
     *
     * @param amount          the amount to convert
     * @param requestCurrency the currency of the request
     * @param targetCurrency  the target currency
     * @return the amount in the targetCurrency
     */
    public BigDecimal convert(BigDecimal amount, Currency requestCurrency, Currency targetCurrency) {
        String url = buildUrl(requestCurrency.getCurrencyCode());
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        if (response == null || response.getResult() == null || !response.getResult().equals("success")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot get exchange rates.");
        }
        Map<String, Double> rates = response.getConversionRates();
        if (!rates.containsKey(targetCurrency.getCurrencyCode())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot convert currency.");
        }
        return BigDecimal.valueOf(rates.get(targetCurrency.getCurrencyCode())).multiply(amount);

    }

    private String buildUrl(String currencyCode) {
        return rootURI + "/" + apiKey + "/latest/" + currencyCode;
    }

}
