package com.michaelvol.bankingapp.cards.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.cards.dto.IssueCardRequestDto;
import com.michaelvol.bankingapp.cards.dto.IssueCardResponseDto;
import com.michaelvol.bankingapp.cards.service.def.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_BASE_URL + "/cards")
@Tag(name = "Payment Cards API", description = "Methods for HTTP card requests")
public class CardController {

    private final CardService cardService;
    private final MessageSource messageSource;

    @PostMapping("/issue")
    @Operation(summary = "Issue a card", description = "Method for issuing a card to an account")
    public ResponseEntity<IssueCardResponseDto> issueCard(@Valid @RequestBody IssueCardRequestDto request) {
        cardService.issue(request);
    }
}
