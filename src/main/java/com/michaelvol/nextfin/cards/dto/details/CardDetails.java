package com.michaelvol.nextfin.cards.dto.details;

import com.michaelvol.nextfin.cards.enums.CardType;

import java.math.BigInteger;

public record CardDetails(CardType cardType, BigInteger spendingLimit) {
}
