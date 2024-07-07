package com.michaelvol.bankingapp.cards.dto;

import com.michaelvol.bankingapp.cards.enums.CardType;

public record IssueCardRequestDto(Long accountId,
                                  CardType cardType) {
}
