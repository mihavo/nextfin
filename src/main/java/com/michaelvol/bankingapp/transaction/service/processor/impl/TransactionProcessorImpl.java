package com.michaelvol.bankingapp.transaction.service.processor.impl;

import com.michaelvol.bankingapp.account.dto.DepositAmountRequestDto;
import com.michaelvol.bankingapp.account.dto.WithdrawAmountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.config.concurrent.props.TransactionProperties;
import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.repository.TransactionRepository;
import com.michaelvol.bankingapp.transaction.service.processor.TransactionProcessor;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Currency;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 30)
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@EnableConfigurationProperties(TransactionProperties.class)
public class TransactionProcessorImpl implements TransactionProcessor {

    private final TransactionRepository transactionRepository;

    @Lazy private final AccountService accountService;

    @Override
    public void doTransaction(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        Account targetAccount = transaction.getTargetAccount();
        Currency currency = transaction.getCurrency();
        BigDecimal amount = transaction.getAmount();
        //Convert amount to target & source account's default currency
        CurrencyConversion requestToTargetConversion = MonetaryConversions.getConversion(targetAccount.getCurrency()
                                                                                                      .getCurrencyCode());
        CurrencyConversion requestToSourceConversion = MonetaryConversions.getConversion(sourceAccount.getCurrency()
                                                                                                      .getCurrencyCode());
        Money requestedCurrencyAmount = Money.of(amount, currency.getCurrencyCode());
        Money targetCurrencyAmount = requestedCurrencyAmount.with(requestToTargetConversion); //amount in source account's currency
        Money sourceCurrencyAmount = requestedCurrencyAmount.with(requestToSourceConversion); //amount in target account's currency


        accountService.withdrawAmount(sourceAccount.getId(),
                                      new WithdrawAmountRequestDto(sourceCurrencyAmount.getNumber().numberValue(
                                              BigDecimal.class)));
        accountService.depositAmount(targetAccount.getId(),
                                     new DepositAmountRequestDto(targetCurrencyAmount.getNumber()
                                                                                     .numberValue(BigDecimal.class)));
    }

    @Override
    public Transaction process(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);
        submitTransactionTask(transaction);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    private void submitTransactionTask(Transaction transaction) {
        doTransaction(transaction);
    }
}
