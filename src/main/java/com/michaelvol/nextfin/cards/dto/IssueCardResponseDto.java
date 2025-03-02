package com.michaelvol.nextfin.cards.dto;

import com.michaelvol.nextfin.cards.entity.Card;

public record IssueCardResponseDto(
        Card card,
        String message) {
}
