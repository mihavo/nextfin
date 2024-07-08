package com.michaelvol.bankingapp.cards.service.impl;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.cards.dto.IssueCardRequestDto;
import com.michaelvol.bankingapp.cards.dto.details.CardDetails;
import com.michaelvol.bankingapp.cards.entity.Card;
import com.michaelvol.bankingapp.cards.enums.CardStatus;
import com.michaelvol.bankingapp.cards.repository.CardRepository;
import com.michaelvol.bankingapp.cards.service.def.CardDataService;
import com.michaelvol.bankingapp.cards.service.def.CoreCardService;
import com.michaelvol.bankingapp.common.address.entity.Address;
import com.michaelvol.bankingapp.common.address.service.def.AddressService;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;

/**
 * Main service for executing card-related operations
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CoreCardServiceImpl implements CoreCardService {

    private final AccountService accountService;
    private final CardDataService cardDataService;
    private final AddressService addressService;
    private final CardRepository cardRepository;
    private final MessageSource messageSource;

    @Value("${finance.spending-limit}")
    private Long defaultSpendingLimit;

    @Override
    public Card issue(Long accountId, CardDetails cardDetails) {
        Account account = accountService.getAccount(accountId);
        if (!account.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new BadRequestException(messageSource.getMessage("account.notactive", new Long[]{account.getId()},
                                                                   LocaleContextHolder.getLocale()));
        }
        Address address = account.getHolder().getAddress();
        String fullName = account.getHolder().getFullName();
        String cardNumber = cardDataService.generateNumber();
        Long cvv = cardDataService.generateCvv();
        LocalDate expirationDate = cardDataService.generateExpirationDate();
        Card card = Card.builder()
                        .account(account)
                        .cardNumber(cardNumber)
                        .billingAddress(address)
                        .cardholderName(fullName)
                        .expirationDate(expirationDate)
                        .status(CardStatus.ACTIVE)
                        .type(cardDetails.cardType())
                        .cvv(cvv)
                        .build();
        return cardRepository.save(card);
    }

    @Override
    public Card issue(IssueCardRequestDto request) {
        CardDetails cardDetails = new CardDetails(request.cardType(), BigInteger.valueOf(defaultSpendingLimit));
        return issue(request.accountId(), cardDetails);
    }
}
