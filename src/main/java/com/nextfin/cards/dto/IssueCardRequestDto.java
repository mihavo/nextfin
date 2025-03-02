package com.nextfin.cards.dto;

import com.nextfin.cards.enums.CardType;

public record IssueCardRequestDto(Long accountId,
                                  CardType cardType) {
}
