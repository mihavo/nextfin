package com.nextfin.cards.dto.details;

import com.nextfin.cards.enums.CardType;

import java.math.BigInteger;

public record CardDetails(CardType cardType, BigInteger spendingLimit) {
}
