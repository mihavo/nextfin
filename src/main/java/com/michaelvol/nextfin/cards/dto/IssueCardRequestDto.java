package com.michaelvol.nextfin.cards.dto;

import com.michaelvol.nextfin.cards.enums.CardType;

public record IssueCardRequestDto(Long accountId,
                                  CardType cardType) {
}
