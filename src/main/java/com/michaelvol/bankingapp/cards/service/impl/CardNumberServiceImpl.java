package com.michaelvol.bankingapp.cards.service.impl;

import com.michaelvol.bankingapp.cards.service.def.CardNumberService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardNumberServiceImpl implements CardNumberService {

    private final Faker faker;


    @Override
    public String generate() {
        return "";
    }

    @Override
    public boolean validate(String cardNumber) {
        return false;
    }
}
