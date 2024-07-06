package com.michaelvol.bankingapp.cards.service.def;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.cards.dto.IssueCardRequestDto;
import com.michaelvol.bankingapp.cards.dto.details.CardDetails;

public interface CardService {

    /**
     * Business logic for issuing a card to an account
     * @param account     the account
     * @param cardDetails the card details
     */
    void issue(Account account, CardDetails cardDetails);

    /**
     * helper controller method to convert and delegate to the main issue method
     * @param request the request dto
     */
    void issue(IssueCardRequestDto request);
}
