package com.michaelvol.bankingapp.cards.service.impl;

import com.michaelvol.bankingapp.cards.service.def.CardNumberService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import net.datafaker.providers.base.Finance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardNumberServiceImpl implements CardNumberService {

    private final Faker faker;

    @Value("${finance.card.issuer}")
    private String creditCardIssuer;

    @Override
    public String generate() {
        return faker.finance().creditCard(Finance.CreditCardType.valueOf(creditCardIssuer));
    }

    @Override
    public boolean validate(String cardNumber) {
        return cardNumber.matches(" ^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]*$.");
    }
}
