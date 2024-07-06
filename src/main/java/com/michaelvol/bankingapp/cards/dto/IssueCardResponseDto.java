package com.michaelvol.bankingapp.cards.dto;

import com.michaelvol.bankingapp.cards.entity.Card;

public record IssueCardResponseDto(
        Card card,
        String message) {
}
