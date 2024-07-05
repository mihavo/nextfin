package com.michaelvol.bankingapp.cards.dto.details;

import com.michaelvol.bankingapp.cards.enums.CardType;

import java.math.BigInteger;

public record CardDetails(CardType cardType, BigInteger spendingLimit) {
}
