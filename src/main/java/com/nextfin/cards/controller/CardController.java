package com.nextfin.cards.controller;

import com.nextfin.AppConstants;
import com.nextfin.cards.dto.IssueCardRequestDto;
import com.nextfin.cards.dto.IssueCardResponseDto;
import com.nextfin.cards.entity.Card;
import com.nextfin.cards.service.def.CoreCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_BASE_URL + "/cards")
@Tag(name = "Payment Cards API", description = "Methods for HTTP card requests")
public class CardController {

    private final CoreCardService cardService;
    private final MessageSource messageSource;

    @PostMapping("/issue")
    @Operation(summary = "Issue a card", description = "Method for issuing a card to an account")
    public ResponseEntity<IssueCardResponseDto> issueCard(@Valid @RequestBody IssueCardRequestDto request) {
        Card card = cardService.issue(request);
        return new ResponseEntity<>(new IssueCardResponseDto(card,
                                                             messageSource.getMessage("card.issue-success",
                                                                                      new UUID[]{card.getId()},
                                                                                      LocaleContextHolder.getLocale())),
                                    HttpStatus.CREATED);
    }
}
