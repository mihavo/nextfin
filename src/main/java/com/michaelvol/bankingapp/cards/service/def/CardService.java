package com.michaelvol.bankingapp.cards.service.def;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.cards.dto.details.CardDetails;

public interface CardService {

    void issue(Account account, CardDetails cardDetails);
}
