package com.michaelvol.bankingapp.cards.service.impl;

import com.michaelvol.bankingapp.cards.service.def.CardNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardNumberServiceImpl implements CardNumberService {

    @Override
    public String generate() {
        return "";
    }

    @Override
    public boolean validate(String cardNumber) {
        return false;
    }
}
