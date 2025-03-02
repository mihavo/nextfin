package com.michaelvol.nextfin.account.controller;

import com.michaelvol.nextfin.AppConstants;
import com.michaelvol.nextfin.account.dto.*;
import com.michaelvol.nextfin.account.entity.Account;
import com.michaelvol.nextfin.account.enums.AccountType;
import com.michaelvol.nextfin.account.service.core.AccountService;
import com.michaelvol.nextfin.holder.service.HolderService;
import com.michaelvol.nextfin.transaction.dto.GetTransactionOptions;
import com.michaelvol.nextfin.transaction.entity.Transaction;
import com.michaelvol.nextfin.transaction.service.core.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/accounts")
@Tag(name = "Accounts API", description = "Methods for account operations")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final MessageSource messageSource;
    private final HolderService holderService;

    @PostMapping
    @Operation(summary = "Account initialization", description = "Method for creating accounts for non-employees i.e. holders")
    public ResponseEntity<CreateAccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto dto) {
        Account account = accountService.createAccount(dto);
        CreateAccountResponseDto responseDto = new CreateAccountResponseDto(account.getId(),
                                                                            "Your account has been created successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets account data", description = "Method for fetching account information")
    public ResponseEntity<GetAccountResponseDto> getAccountData(@PathVariable(name = "id") @NotNull @Positive Long accountId) {
        Account account = accountService.getAccount(accountId);
        GetAccountResponseDto responseDto = new GetAccountResponseDto(account, "Fetched account successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<GetMyAccountsResponseDto> getMyAccounts(@RequestParam(name = "type", required = false) AccountType type) {
        List<Account> accounts = holderService.getAccounts(type);
        return new ResponseEntity<>(new GetMyAccountsResponseDto(accounts, messageSource.getMessage("account" +
                ".get-accounts-success", new Integer[]{accounts.size()}, LocaleContextHolder.getLocale())), HttpStatus.OK);
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "Deposit to account", description = "Method for depositing a specified amount to an account")
    public ResponseEntity<DepositAmountResponseDto> depositAmount(@PathVariable @NotNull Long id, @Valid @RequestBody DepositAmountRequestDto dto) {
        BigDecimal updatedBalance = accountService.depositAmount(id, dto);
        return new ResponseEntity<>(new DepositAmountResponseDto(updatedBalance,
                                                                 messageSource.getMessage("account.deposit.success",
                                                                                          null,
                                                                                          LocaleContextHolder.getLocale())),
                                    HttpStatus.OK);
    }

    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw from account", description = "Method for withdrawing a specified amount from an account")
    public ResponseEntity<WithdrawAmountResponseDto> withdrawAmount(@PathVariable @NotNull Long id, @Valid @RequestBody WithdrawAmountRequestDto dto) {
        BigDecimal updatedBalance = accountService.withdrawAmount(id, dto);
        return new ResponseEntity<>(new WithdrawAmountResponseDto(updatedBalance,
                                                                  messageSource.getMessage(
                                                                          "account.withdraw.success",
                                                                          null,
                                                                          LocaleContextHolder.getLocale())),
                                    HttpStatus.OK);
    }

    @GetMapping("/{id}/balance")
    @Operation(summary = "Gets account balance", description = "Method for fetching current account balance")
    public ResponseEntity<AccountBalanceResponseDto> checkBalance(@PathVariable @Min(value = 0L, message = "The value" +
            " must be positive") Long id) {
        GetAccountBalanceDto dto = accountService.checkBalance(id);
        String message = messageSource.getMessage("account.balance.check",
                                                  new String[]{dto.balance().toString(), dto.currency().getSymbol()},
                                                  LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new AccountBalanceResponseDto(dto.balance(), dto.currency(), message),
                                    HttpStatus.OK);
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Gets recent transactions of the account")
    public ResponseEntity<Page<Transaction>> getTransactions(@PathVariable @NotNull Long id, @Valid GetTransactionOptions options) {
        Account account = accountService.getAccount(id);
        Page<Transaction> filteredTransactions = transactionService.getAccountTransactions(account, options);
        return new ResponseEntity<>(filteredTransactions, HttpStatus.OK);
    }

    @PatchMapping("/{id}/transactions/update-limit")
    @Operation(summary = "Updates transaction limit", description = "Method for updating transaction limit of an account")
    public ResponseEntity<UpdateTransactionLimitResponseDto> updateTransactionLimit(@PathVariable @NotNull Long id, @Valid @RequestBody UpdateTransactionLimitRequestDto dto) {
        Account account = accountService.getAccount(id);
        accountService.updateTransactionLimit(account, dto.transactionLimit());
        return new ResponseEntity<>(new UpdateTransactionLimitResponseDto(dto.transactionLimit()), HttpStatus.OK);
    }

    @PatchMapping("/{id}/transactions/toggle-limit")
    @Operation(summary = "Toggles transaction limit", description = "Method for toggling transaction limit of an account")
    public ResponseEntity<ToggleTransactionLimitResponseDto> toggleTransactionLimit(@PathVariable @NotNull Long id) {
        Account account = accountService.getAccount(id);
        Boolean enabled = accountService.toggleTransactionLimit(account);
        String message = messageSource.getMessage("account.transaction.limit.toggle",
                                                  new String[]{enabled.toString()},
                                                  LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ToggleTransactionLimitResponseDto(enabled, message), HttpStatus.OK);
    }

    @PatchMapping("/{id}/transactions/toggle-2fa")
    @Operation(summary = "Toggles transaction 2FA", description = "Method for toggling transaction 2FA of an account")
    public ResponseEntity<ToggleTransaction2FADto> toggleTransactionOTP(@PathVariable @NotNull Long id) {
        Account account = accountService.getAccount(id);
        Boolean enabled = accountService.toggleTransaction2FA(account);
        String message = messageSource.getMessage("account.transaction.2fa.toggle",
                                                  new String[]{enabled ? "enabled" : "disabled"},
                                                  LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ToggleTransaction2FADto(enabled, message), HttpStatus.OK);

    }

    @PatchMapping("/{id}/transactions/toggle-sms-confirmation")
    @Operation(summary = "Toggles transaction SMS confirmation", description = "Method for toggling transaction SMS confirmation of an account")
    public ResponseEntity<ToggleTransactionSMSConfirmationDto> toggleTransactionSMSConfirmation(@PathVariable @NotNull Long id) {
        Account account = accountService.getAccount(id);
        Boolean enabled = accountService.toggleTransactionSMSConfirmation(account);
        String message = messageSource.getMessage("account.transaction.sms-confirmation.toggle",
                new String[]{enabled ? "enabled" : "disabled"},
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ToggleTransactionSMSConfirmationDto(enabled, message), HttpStatus.OK);
    }

}
