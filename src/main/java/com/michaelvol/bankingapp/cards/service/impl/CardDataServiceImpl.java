package com.michaelvol.bankingapp.cards.service.impl;

import com.michaelvol.bankingapp.cards.service.def.CardDataService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.providers.base.Finance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CardDataServiceImpl implements CardDataService {

    private final Faker faker;

    @Value("${finance.card.issuer}")
    private String creditCardIssuer;

    @Value("${finance.card.expiration-date-interval}")
    private Short expirationDateInterval;

    @Override
    public String generateNumber() {
        return faker.finance().creditCard(Finance.CreditCardType.valueOf(creditCardIssuer));
    }

    @Override
    public boolean validate(String cardNumber) {
        return cardNumber.matches(" ^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]*$.");
    }

    @Override
    public Long generateCvv() {
        long cvv = ThreadLocalRandom.current().nextInt(1000);
        if (cvv == 999L || cvv == 0L) { //reject cards that are all 0 or 9
            return generateCvv();
        }
        return cvv;
    }

    @Override
    public LocalDate generateExpirationDate() {
        return LocalDate.now().plusYears(expirationDateInterval);
    }
}
