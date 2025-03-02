package com.michaelvol.nextfin.cards.service.def;

import java.time.LocalDate;

/**
 * Service for card number related operations
 */
public interface CardDataService {
    String generateNumber();

    boolean validate(String cardNumber);

    Long generateCvv();

    LocalDate generateExpirationDate();
}
