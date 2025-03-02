package com.nextfin.cards.service.impl;

import com.nextfin.AppConstants;
import com.nextfin.cards.repository.CardRepository;
import com.nextfin.cards.service.def.CardDataService;
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

    private final CardRepository cardRepository;

    @Override
    public String generateNumber() {
        String cardNumber = faker.finance().creditCard(Finance.CreditCardType.valueOf(creditCardIssuer.toUpperCase()));
        return cardRepository.existsCardByCardNumber(cardNumber) ? generateNumber() : cardNumber;
    }

    @Override
    public boolean validate(String cardNumber) {
        return cardNumber.matches(AppConstants.VISA_REGEX);
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
