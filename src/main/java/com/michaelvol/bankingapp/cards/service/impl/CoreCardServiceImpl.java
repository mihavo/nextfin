package com.michaelvol.bankingapp.cards.service.impl;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.cards.dto.IssueCardRequestDto;
import com.michaelvol.bankingapp.cards.dto.details.CardDetails;
import com.michaelvol.bankingapp.cards.repository.CardRepository;
import com.michaelvol.bankingapp.cards.service.def.CoreCardService;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Main service for executing card-related operations
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CoreCardServiceImpl implements CoreCardService {

    private final AccountService accountService;
    private final CardRepository cardRepository;
    private final MessageSource messageSource;

    @Override
    public void issue(Account account, CardDetails cardDetails) {
        Boolean accountExists = accountService.checkExistence(account.getId());
        if (!accountExists) {
            throw new BadRequestException(messageSource.getMessage("account.notfound", new Long[]{account.getId()},
                                                                   LocaleContextHolder.getLocale()));
        }


    }

    @Override
    public void issue(IssueCardRequestDto request) {

    }
}
