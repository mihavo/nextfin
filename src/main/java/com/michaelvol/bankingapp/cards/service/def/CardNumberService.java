package com.michaelvol.bankingapp.cards.service.def;

/**
 * Service for card number related operations
 */
public interface CardNumberService {
    public String generate();

    public boolean validate(String cardNumber);
}
