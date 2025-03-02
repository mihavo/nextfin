package com.nextfin.cards.dto;

import com.nextfin.cards.entity.Card;

public record IssueCardResponseDto(
        Card card,
        String message) {
}
