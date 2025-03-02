package com.nextfin.cards.service.def;

import com.nextfin.cards.dto.IssueCardRequestDto;
import com.nextfin.cards.dto.details.CardDetails;
import com.nextfin.cards.entity.Card;

public interface CoreCardService {

    /**
     * Business logic for issuing a card to an account
     * @param account     the account
     * @param cardDetails the card details
     */
    Card issue(Long accountId, CardDetails cardDetails);

    /**
     * helper controller method to convert and delegate to the main issue method
     * @param request the request dto
     */
    Card issue(IssueCardRequestDto request);
}
