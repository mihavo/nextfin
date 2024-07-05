package com.michaelvol.bankingapp.cards.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.cards.service.CardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/cards")
@Tag(name = "Payment Cards API", description = "Methods for HTTP card requests")
public class CardController {

    private final CardService cardService;
    private final MessageSource messageSource;
}
