package com.nextfin.transaction.service.executor.embedded.impl;

import com.nextfin.account.dto.DepositAmountRequestDto;
import com.nextfin.account.dto.WithdrawAmountRequestDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.config.concurrent.props.TransactionProperties;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.repository.TransactionRepository;
import com.nextfin.transaction.service.executor.embedded.EmbeddedTransactionExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@EnableConfigurationProperties(TransactionProperties.class)
@ConditionalOnProperty(name = "nextfin.embedded-executor", havingValue = "true", matchIfMissing = true)
@Slf4j
public class EmbeddedTransactionExecutorImpl implements EmbeddedTransactionExecutor {

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
        transaction.setStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);
        log.debug("Transaction with id {} submitted for processing", transaction.getId());
        submitTransactionTask(transaction);
        log.debug("Transaction with id {} completed", transaction.getId());
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    private void submitTransactionTask(Transaction transaction) {
        doTransaction(transaction);
    }
}
